package com.example.myapplication.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.myapplication.GroupChatActivity;
import com.example.myapplication.InboxListActivity;
import com.example.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    private LinearLayout privateMessage,group;


    public ChatFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        privateMessage = rootView.findViewById(R.id.privateMessage);
        group = rootView.findViewById(R.id.group);

        privateMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),InboxListActivity.class);
                startActivity(i);
            }
        });

        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),GroupChatActivity.class);
                startActivity(i);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Chat");
    }

}
