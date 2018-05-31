package com.example.myapplication.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.example.myapplication.SearchActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private Button search, chat, profile, transport;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        search = rootView.findViewById(R.id.search_icon);
        chat = rootView.findViewById(R.id.chat_icon);
        profile = rootView.findViewById(R.id.profile_icon);
        transport = rootView.findViewById(R.id.transport_icon);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
                //SearchFragment nextFrag= new SearchFragment();
                //getActivity().getSupportFragmentManager().beginTransaction()
                //        .replace(getId(), nextFrag,"findThisFragment")
                //        .addToBackStack(null)
                //        .commit();
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatFragment nextFrag= new ChatFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(getId(), nextFrag,"findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment nextFrag= new ProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(getId(), nextFrag,"findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        transport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlankFragment nextFrag= new BlankFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(getId(), nextFrag,"findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Home");
    }
}
