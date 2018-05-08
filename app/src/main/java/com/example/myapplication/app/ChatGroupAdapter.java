package com.example.myapplication.app;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class ChatGroupAdapter extends RecyclerView.Adapter<ChatGroupAdapter.ViewHolder> {

    Context context = null;
    ArrayList<ChatGroupModel> chatList = null;
    private final int SENT_MESSAGE = 0, RECEIVED_MESSAGE = 1;

    public ChatGroupAdapter(Context context, ArrayList<ChatGroupModel> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    /***
     *
     * @param position
     * @return the view type of the item at the position for the
     * purpose of recycling view
     *
     * By default it returns zero showing a single view type for the adapter.
     */
    @Override
    public int getItemViewType(int position) {
        if (chatList.get(position).getIsMe()) {
            return SENT_MESSAGE;
        } else {
            return RECEIVED_MESSAGE;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        //Based on view type decide which type of view to supply with viewHolder
        switch (viewType) {
            case SENT_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_message_group, parent, false);
                break;

            case RECEIVED_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.received_message_group, parent, false);
                break;
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatGroupModel model = chatList.get(position);
        holder.texttosend.setText(model.getMessage());
        holder.date.setText(model.getTime());
        if(model.getIsMe()){
            holder.username.setVisibility(View.GONE);
        }
        else {
            holder.username.setText(model.getUsername());
            holder.username.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    /*
    * Here we have kept ID's of all the child row elements same.
    * But we can also create to different viewHolder classes
    * for different child rows.
    */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView texttosend,username;
        TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            texttosend = (TextView) itemView.findViewById(R.id.message);
            date = (TextView) itemView.findViewById(R.id.date);
            username = itemView.findViewById(R.id.username);
        }
    }
}
