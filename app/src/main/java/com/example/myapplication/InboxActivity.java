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
import com.example.myapplication.app.ChatAdapter;
import com.example.myapplication.app.ChatModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InboxActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private String contact_id,contact_image,contact_active,contact_username,user_id,username;
    private int contact_online;
    private Bundle extras;
    ArrayList<ChatModel> chatlist = null;
    ChatAdapter adapter = null;
    RecyclerView chat_list;
    private EditText editText;
    private ImageView user_image;
    private TextView usernameTXT,active;
    Button chatSendButton;
    LinearLayoutManager layoutManager;
    private Boolean loading=false,isLastPage=false;
    private int pageCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        extras=getIntent().getExtras();
        contact_id=extras.getString("contact_id");
        contact_image=extras.getString("contact_image");
        contact_online=extras.getInt("contact_online");
        contact_active=extras.getString("contact_active");
        contact_username=extras.getString("contact_username");
        user_id=extras.getString("user_id");
        username=extras.getString("username");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        user_image=findViewById(R.id.user_image);
        usernameTXT=findViewById(R.id.username);
        active=findViewById(R.id.active);
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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

        Picasso.with(getApplicationContext()).load(AppConfig.URL_IMAGE+contact_image).into(user_image);
        usernameTXT.setText(contact_username);
        if(contact_online == 1){
            active.setText("Online");
            active.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        else {
            active.setText(contact_active);
        }

        layoutManager = new LinearLayoutManager(this);

        layoutManager.setReverseLayout(true);
        chat_list.setLayoutManager(layoutManager);
        chatlist = new ArrayList<>();
        adapter = new ChatAdapter(getApplicationContext(), chatlist);
        chat_list.setAdapter(adapter);
        getInbox();
        seenChat();

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
                ChatModel myChatModel = new ChatModel(text,true,"Just now");
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
        String tag_string_req = "req_get_inbox";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_CHAT, new Response.Listener<String>() {

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
                            Boolean isMe = jobj.getBoolean("IsMe");

                            ChatModel myChatModel = new ChatModel(message,isMe,date);
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
                params.put("user_id", user_id);
                params.put("username", username);
                params.put("contact_id", contact_id);
                params.put("pagecount", Integer.toString(pageCount));
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void sendChat(final String message) {
        String tag_string_req = "req_send_inbox";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SEND_CHAT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {

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
                params.put("user_id", user_id);
                params.put("username", username);
                params.put("contact_id", contact_id);
                params.put("message", message);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void seenChat() {
        String tag_string_req = "req_seen_chat";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SEEN_CHAT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {

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
                params.put("user_id", user_id);
                params.put("username", username);
                params.put("contact_id", contact_id);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
