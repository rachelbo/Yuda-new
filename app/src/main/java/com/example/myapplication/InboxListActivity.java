package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.app.AppConfig;
import com.example.myapplication.app.AppController;
import com.example.myapplication.app.InboxModel;
import com.example.myapplication.app.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InboxListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<InboxModel> myInboxModelList = new ArrayList<>();
    private MyAdapter mAdapter;
    private Boolean loading = true;
    private Boolean isLastPage = false;
    private int pageCount=0;
    private SessionManager session;
    private TextView textView;

    private ProgressBar indeterminate_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_list);
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        textView = findViewById(R.id.toolbar_title);
        textView.setText("Chat");
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRecyclerView = findViewById(R.id.recycleView);
        mRecyclerView.setHasFixedSize(false);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(myInboxModelList);
        mRecyclerView.setAdapter(mAdapter);
        indeterminate_progress= findViewById(R.id.indeterminate_progress);
        session = new SessionManager(getApplicationContext());
        getMyInbox();


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = mLayoutManager.findLastVisibleItemPosition();

                if (lastvisibleitemposition == mAdapter.getItemCount() - 1) {

                    if (!loading && !isLastPage) {

                        loading = true;
                        getMyInbox();

                    }
                }
            }
        });
    }

    private void getMyInbox() {
        String tag_string_req = "req_get_my_inbox";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.GET_MY_INBOX, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                indeterminate_progress.setVisibility(View.GONE);
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    int totalpage = jObj.getInt("total_count");
                    pageCount = jObj.getInt("page_count");

                    if (success == 1) {
                        JSONArray ja = jObj.getJSONArray("inbox");
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jobj = ja.getJSONObject(i);
                            String contact_id = jobj.getString("contact_id");
                            String contact_image = jobj.getString("contact_image");
                            int contact_online = jobj.getInt("contact_online");
                            String contact_active = jobj.getString("contact_active");
                            String image = jobj.getString("image");
                            String username = jobj.getString("username");
                            String time = jobj.getString("time");
                            String message = jobj.getString("message");
                            String unread = jobj.getString("unread");
                            InboxModel myInboxModel = new InboxModel(contact_id,image,username,time,message,unread,contact_active,contact_image,contact_online);
                            myInboxModelList.add(myInboxModel);

                        }
                        mAdapter.notifyDataSetChanged();
                        loading = false;
                        if(totalpage <= pageCount){
                            isLastPage=true;
                        }
                        else {
                            isLastPage=false;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (pageCount ==0){
                    indeterminate_progress.setVisibility(View.GONE);
                    isLastPage=true;
                }
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", session.getId());
                params.put("username", session.getUsername());
                params.put("pagecount", Integer.toString(pageCount));
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int TYPE_ITEM = 0;
        private static final int TYPE_FOOTER = 1;
        private List<InboxModel> myInboxModelList;
        private InboxModel myOffer;

        public MyAdapter(List<InboxModel> mList) {
            this.myInboxModelList = mList;
        }

        @Override
        public int getItemViewType(int position) {
            if(isPositionFooter(position)){
                return TYPE_FOOTER;
            }

            return TYPE_ITEM;
        }
        private boolean isPositionFooter(int position) {
            return position > myInboxModelList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            if (viewType == TYPE_ITEM) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_inboxs, viewGroup, false);
                return new ItemViewHolder(view);

            }
            else if (viewType == TYPE_FOOTER) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loading,
                        viewGroup, false);
                return new FooterViewHolder(view);

            }

            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof ItemViewHolder) {
                myOffer = myInboxModelList.get(position);

                Picasso.with(getApplicationContext()).load(AppConfig.URL_IMAGE+myOffer.getImage()).into(((ItemViewHolder) holder).profile_image);
                ((ItemViewHolder) holder).usernameTXT.setText(myOffer.getUsername());
                ((ItemViewHolder) holder).time.setText(myOffer.getTime());
                ((ItemViewHolder) holder).message.setText(myOffer.getMessage());
                ((ItemViewHolder) holder).count.setText(myOffer.getUnread());

                if(Integer.parseInt(myOffer.getUnread()) >0){
                    ((ItemViewHolder) holder).count.setVisibility(View.VISIBLE);
                }
                else {
                    ((ItemViewHolder) holder).count.setVisibility(View.GONE);
                }


            }
            else if (holder instanceof FooterViewHolder) {
                FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
                footerViewHolder.progressBar.setIndeterminate(true);
                if (isLastPage){
                    footerViewHolder.progressBar.setVisibility(View.GONE);
                }
                else {
                    footerViewHolder.progressBar.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public int getItemCount() {
            return this.myInboxModelList.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            public View View;
            public ImageView profile_image;
            public TextView usernameTXT,message,time,count;
            public ItemViewHolder(View v) {
                super(v);
                View = v;
                v.setOnClickListener(this);
                profile_image= View.findViewById(R.id.profile_image);
                usernameTXT=View.findViewById(R.id.username);
                message=View.findViewById(R.id.message);
                time=View.findViewById(R.id.time);
                count=View.findViewById(R.id.count);
            }

            @Override
            public void onClick(View view) {
                myOffer = myInboxModelList.get(getAdapterPosition());
                Intent intent = new Intent(getApplicationContext(), InboxActivity.class);
                intent.putExtra("contact_id",myOffer.getContactId());
                intent.putExtra("contact_image",myOffer.getContact_image());
                intent.putExtra("contact_online",myOffer.getContact_online());
                intent.putExtra("contact_active",myOffer.getContact_active());
                intent.putExtra("contact_username",myOffer.getUsername());
                intent.putExtra("user_id",session.getId());
                intent.putExtra("username",session.getUsername());
                startActivity(intent);
            }
        }

        public class FooterViewHolder extends RecyclerView.ViewHolder {
            public View View;
            public ProgressBar progressBar;
            public FooterViewHolder(View v) {
                super(v);
                View = v;
                progressBar = (ProgressBar) itemView.findViewById(R.id.indeterminate_progress);
            }

        }
    }
}
