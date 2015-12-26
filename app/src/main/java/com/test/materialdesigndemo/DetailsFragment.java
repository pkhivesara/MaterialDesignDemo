package com.test.materialdesigndemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DetailsFragment extends Fragment {

    TextView raceNumberTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        raceNumberTextView = (TextView) view.findViewById(R.id.raceNumberTextView);
        return view;

    }

    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }

}
