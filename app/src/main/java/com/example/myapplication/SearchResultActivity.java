package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private int start;
    private Boolean isLastPage;
    private Boolean load_more= true;
    private List<SearchModel> myModelList = new ArrayList<>();
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        textView = findViewById(R.id.toolbar_title);
        textView.setText("Search Result");

        recyclerView =findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(myModelList);
        recyclerView.setAdapter(mAdapter);
        myModelList.clear();
        start=0;
        isLastPage = false;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = mLayoutManager.findLastVisibleItemPosition();

                if (lastvisibleitemposition == mAdapter.getItemCount() - 1) {

                    if (!load_more && !isLastPage) {
                        load_more = true;
                        //get_search_result();
                    }
                }
            }
        });

//        myModelList.add(new SearchModel("","everton","4, green street","40C","78","yuyu","19:30-00:30","crave","02-6272830","gova,edu, vegitab","facebook.com", "", longitude));
//        myModelList.add(new SearchModel("","everton","4, green street","40C","78","yuyu","19:30-00:30","crave","02-6272830","gova,edu, vegitab","facebook.com", lat, longitude));
//        myModelList.add(new SearchModel("","everton","4, green street","40C","78","yuyu","19:30-00:30","crave","02-6272830","gova,edu, vegitab","facebook.com", lat, longitude));
//        myModelList.add(new SearchModel("","everton","4, green street","40C","78","yuyu","19:30-00:30","crave","02-6272830","gova,edu, vegitab","facebook.com", lat, longitude));
//        myModelList.add(new SearchModel("","everton","4, green street","40C","78","yuyu","19:30-00:30","crave","02-6272830","gova,edu, vegitab","facebook.com", lat, longitude));
//        myModelList.add(new SearchModel("","everton","4, green street","40C","78","yuyu","19:30-00:30","crave","02-6272830","gova,edu, vegitab","facebook.com", lat, longitude));
//        myModelList.add(new SearchModel("","everton","4, green street","40C","78","yuyu","19:30-00:30","crave","02-6272830","gova,edu, vegitab","facebook.com", lat, longitude));
//        myModelList.add(new SearchModel("","everton","4, green street","40C","78","yuyu","19:30-00:30","crave","02-6272830","gova,edu, vegitab","facebook.com", lat, longitude));
//        myModelList.add(new SearchModel("","everton","4, green street","40C","78","yuyu","19:30-00:30","crave","02-6272830","gova,edu, vegitab","facebook.com", lat, longitude));
//        myModelList.add(new SearchModel("","everton","4, green street","40C","78","yuyu","19:30-00:30","crave","02-6272830","gova,edu, vegitab","facebook.com", lat, longitude));
        mAdapter.notifyDataSetChanged();

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
            public TextView access,descriptionTxt,usernameTxt,video_number;
            public ImageView imageView;
            public ItemViewHolder(View v) {
                super(v);
                v.setOnClickListener(this);
                usernameTxt = v.findViewById(R.id.username);


            }

            @Override
            public void onClick(View view) {
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
