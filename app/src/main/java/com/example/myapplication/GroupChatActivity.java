package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.app.AppConfig;
import com.example.myapplication.app.AppController;
import com.example.myapplication.app.ChatGroupAdapter;
import com.example.myapplication.app.ChatGroupModel;
import com.example.myapplication.app.ChatModel;
import com.example.myapplication.app.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GroupChatActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    ArrayList<ChatGroupModel> chatlist = null;
    ChatGroupAdapter adapter = null;
    RecyclerView chat_list;
    private EditText editText;
    Button chatSendButton;
    LinearLayoutManager layoutManager;
    private Boolean loading=false,isLastPage=false;
    private int pageCount=0;
    private SessionManager session;
    private ScheduledExecutorService scheduler=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        toolbar =findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        setSupportActionBar(toolbar);

        chat_list = (RecyclerView) findViewById(R.id.chat_list);
        chatSendButton = (Button) findViewById(R.id.chatSendButton);
        editText = (EditText) findViewById(R.id.texttosend);
        chatSendButton.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        session = new SessionManager(getApplicationContext());

        if(session.isChatIn()){
            chatSendButton.setVisibility(View.VISIBLE);
        }
        else {
            chatSendButton.setVisibility(View.GONE);
        }


        layoutManager = new LinearLayoutManager(this);

        layoutManager.setReverseLayout(true);
        chat_list.setLayoutManager(layoutManager);
        chatlist = new ArrayList<>();
        adapter = new ChatGroupAdapter(getApplicationContext(), chatlist);
        chat_list.setAdapter(adapter);
        getInbox();
        startTimer();

        chat_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                int lastvisibleitemposition = layoutManager.findLastVisibleItemPosition();

                if (lastvisibleitemposition == adapter.getItemCount() - 1) {

                    if (!loading && !isLastPage) {

                        loading = true;
                        getInbox();

                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.chatSendButton) {
            String text = null;
            if (editText != null) {
                text = editText.getText().toString().trim();
            }
            int count = adapter.getItemCount();
            if (text != null && text.length() != 0) {
                ChatGroupModel myChatModel = new ChatGroupModel(text,"",true,"Just now");
                chatlist.add(0,myChatModel);
                adapter.notifyItemInserted(0);
                sendChat(text);
            /*
            * void scrollToPosition(int position) tells layout manager to scroll recyclerView
            * to given position
            */
                chat_list.scrollToPosition(0);
                editText.setText("");
            }
        }
    }

    private void getInbox() {
        String tag_string_req = "req_get_group_chat";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_GROUP_CHAT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    int totalpage = jObj.getInt("total_count");
                    pageCount = jObj.getInt("page_count");
                    if (success == 1) {
                        JSONArray ja = jObj.getJSONArray("chats");
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jobj = ja.getJSONObject(i);
                            String message = jobj.getString("message");
                            String date = jobj.getString("Date");
                            String username = jobj.getString("username");
                            Boolean isMe = jobj.getBoolean("IsMe");

                            ChatGroupModel myChatModel = new ChatGroupModel(message,username,isMe,date);
                            chatlist.add(myChatModel);

                        }
                        adapter.notifyDataSetChanged();
                        //chat_list.smoothScrollToPosition(lenght_before);
                    }
                    loading = false;
                    if(totalpage <= pageCount){
                        isLastPage=true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", session.getId());
                params.put("pagecount", Integer.toString(pageCount));
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void sendChat(final String message) {
        String tag_string_req = "req_send_inbox";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SEND_GROUP_CHAT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {
                        session.setChat(false);
                        chatSendButton.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("message", message);
                params.put("user_id", session.getId());
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void startTimer(){
        if(scheduler == null){
            scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            session.setChat(true);
                        }
                    });
                }
            }, 5, 5, TimeUnit.MINUTES);

        }
    }
}
