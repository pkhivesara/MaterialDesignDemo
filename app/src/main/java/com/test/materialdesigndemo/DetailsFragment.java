package com.test.materialdesigndemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Pratik on 12/21/15.
 */
public class DetailsFragment extends Fragment {

    TextView raceNumberTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        int raceNumber = bundle.getInt("position", 0);
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        raceNumberTextView = (TextView) view.findViewById(R.id.raceNumberTextView);
        raceNumberTextView.setText(Integer.toString(raceNumber));
        return view;

    }

    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }

}
