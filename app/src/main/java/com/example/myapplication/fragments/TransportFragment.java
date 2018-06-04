package com.example.myapplication.fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;


public class TransportFragment extends Fragment {
    TextView mTextView1, mTextView2;

    public TransportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transport, container, false);
        mTextView1 = rootView.findViewById(R.id.textView3);
        mTextView2 = rootView.findViewById(R.id.textView4);
        mTextView1.setText(R.string.public_trans);
        mTextView2.setText(R.string.parking);
        // Inflate the layout for this fragment

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Public transport");
    }


}
