package com.test.materialdesigndemo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;



public class DetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DetailsFragment detailsFragment = DetailsFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("episode",getIntent().getStringExtra("episode"));
        detailsFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.container_layout,detailsFragment);
        fragmentTransaction.commit();

    }

}
