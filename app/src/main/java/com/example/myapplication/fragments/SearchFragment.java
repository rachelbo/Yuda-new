package com.example.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.R;
import com.example.myapplication.SearchModel;
import com.example.myapplication.ShopDetailActivity;
import com.example.myapplication.app.AppConfig;
import com.example.myapplication.app.AppController;
import com.example.myapplication.app.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private Boolean load_more= true,lastPage = false;
    private ProgressBar indeterminate_progress;
    private List<SearchModel> myModelList = new ArrayList<>();
    private int start = 0;
    private int total = 0;
    private SessionManager session;
    private String query;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        query= getArguments().getString("query");

        recyclerView =rootView.findViewById(R.id.recyclerView);
        indeterminate_progress = rootView.findViewById(R.id.indeterminate_progress);
        recyclerView.setHasFixedSize(false);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(myModelList);
        recyclerView.setAdapter(mAdapter);
        session= new SessionManager(getContext());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = mLayoutManager.findLastVisibleItemPosition();

                if (lastvisibleitemposition == mAdapter.getItemCount() - 1) {
                    if (!load_more && !lastPage) {
                        load_more = true;
                        getMarket();
                    }

                }
            }
        });
        getMarket();
        return rootView;
    }

    private void getMarket() {
        indeterminate_progress.setVisibility(View.VISIBLE);
        String tag_string_req = "req_get_search";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SEARCH, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                indeterminate_progress.setVisibility(View.GONE);
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    total = jObj.getInt("next");
                    start = start+20;

                    if (success==1) {
                        JSONArray ja = jObj.getJSONArray("results");
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jobj = ja.getJSONObject(i);
                            String name = jobj.getString("name");
                            String image = jobj.getString("image");
                            String address = jobj.getString("address");
                            String atmosphere = jobj.getString("atmosphere");
                            String class_shop = jobj.getString("class_shop");
                            String average_price = jobj.getString("average_price");
                            String hours = jobj.getString("hours");
                            String menu = jobj.getString("menu");
                            String phone = jobj.getString("phone");
                            String tags = jobj.getString("tags");
                            String website = jobj.getString("phone");
                            String lat = jobj.getString("lat");
                            String longitude = jobj.getString("longitude");
                            SearchModel myModel = new SearchModel(image,name,address,atmosphere,average_price,class_shop,hours,menu,phone,tags,website, lat, longitude);
                            myModelList.add(myModel);
                        }
                        mAdapter.notifyDataSetChanged();
                        load_more=false;
                    }

                    if (start >= total){
                        lastPage=true;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                indeterminate_progress.setVisibility(View.GONE);
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("start", String.valueOf(start));
                params.put("type", session.getMarket_Table());
                params.put("query", query);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;
        private static final int TYPE_FOOTER = 2;
        private List<SearchModel> myModelList;
        private SearchModel myModel;

        public MyAdapter(List<SearchModel> mList) {
            this.myModelList = mList;
        }

        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position)) {
                return TYPE_HEADER;

            }
            else if(isPositionFooter(position)){
                return TYPE_FOOTER;
            }

            return TYPE_ITEM;
        }
        private boolean isPositionHeader(int position) {
            return position == 0;
        }
        private boolean isPositionFooter(int position) {
            return position > myModelList.size()-0;
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            if (viewType == TYPE_ITEM) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_shop, viewGroup, false);
                return new ItemViewHolder(view);
            }
            else if (viewType == TYPE_HEADER) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_header, viewGroup, false);
                return new HeaderViewHolder(view);

            }
            else if (viewType == TYPE_FOOTER) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loading, viewGroup, false);
                return new FooterViewHolder(view);
            }

            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

        }
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof ItemViewHolder) {
                myModel = myModelList.get(position-1);
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                itemViewHolder.name.setText(myModel.getName());
                itemViewHolder.atmosphere.setText(myModel.getAtmosphere());
                itemViewHolder.average_priceV.setText(myModel.getAverage_price());
                itemViewHolder.hours.setText(myModel.getHours());
                itemViewHolder.address.setText(myModel.getAddress());
                Picasso.with(getContext()).load(myModel.getImage()).into(itemViewHolder.imageView);


            }
            else if (holder instanceof FooterViewHolder) {
                FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
                footerViewHolder.progressBar.setIndeterminate(true);
                if (true){
                    footerViewHolder.progressBar.setVisibility(View.GONE);
                }
                else {
                    footerViewHolder.progressBar.setVisibility(View.VISIBLE);
                }
            }
        }
        @Override
        public int getItemCount() {
            return this.myModelList.size()+2;
        }

        class HeaderViewHolder extends RecyclerView.ViewHolder{
            public HeaderViewHolder(View itemView) {
                super(itemView);
            }
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            public TextView name,average_priceV,hours,address,atmosphere;
            public ImageView imageView;
            public ItemViewHolder(View v) {
                super(v);
                atmosphere = v.findViewById(R.id.atmosphere);
                name = v.findViewById(R.id.name);
                average_priceV = v.findViewById(R.id.average_priceV);
                hours = v.findViewById(R.id.hours);
                address = v.findViewById(R.id.address);
                imageView = v.findViewById(R.id.imageView);
                v.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                myModel = myModelList.get(getAdapterPosition()-1);
                Intent showPhotoIntent = new Intent(getContext(), ShopDetailActivity.class);
                showPhotoIntent.putExtra("name", myModel.getName());
                showPhotoIntent.putExtra("image", myModel.getImage());
                showPhotoIntent.putExtra("address", myModel.getAddress());
                showPhotoIntent.putExtra("atmosphere", myModel.getAtmosphere());
                showPhotoIntent.putExtra("average_price", myModel.getAverage_price());
                showPhotoIntent.putExtra("class", myModel.getClass_shop());
                showPhotoIntent.putExtra("hours", myModel.getHours());
                showPhotoIntent.putExtra("menu", myModel.getMenu());
                showPhotoIntent.putExtra("phone", myModel.getPhone());
                showPhotoIntent.putExtra("tags", myModel.getTags());
                showPhotoIntent.putExtra("website", myModel.getWebsite());
                showPhotoIntent.putExtra("lon", myModel.getLongitude());
                showPhotoIntent.putExtra("lat", myModel.getLat());
                startActivity(showPhotoIntent);
            }
        }

        public class FooterViewHolder extends RecyclerView.ViewHolder {
            public View View;
            public ProgressBar progressBar;
            public FooterViewHolder(View v) {
                super(v);
                progressBar =v.findViewById(R.id.indeterminate_progress);
            }

        }

    }

}
