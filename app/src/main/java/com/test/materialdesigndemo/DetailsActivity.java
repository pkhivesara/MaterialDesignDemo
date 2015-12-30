package com.test.materialdesigndemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;


public class DetailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView detailsActivityImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        detailsActivityImageView = (ImageView) findViewById(R.id.detailsActivityImageView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Details View");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DetailsFragment detailsFragment = DetailsFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("episode", getIntent().getStringExtra("episode"));
        bundle.putString("season", getIntent().getStringExtra("season"));


        bundle.putString("title", getIntent().getStringExtra("title"));
        detailsFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.container_layout, detailsFragment);
        fragmentTransaction.commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(loadCompleteBroadcastReceiver,new IntentFilter("LOAD_COMPLETE"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(loadCompleteBroadcastReceiver);
    }

    public BroadcastReceiver loadCompleteBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String posterURL = intent.getStringExtra("url");
            Picasso.with(DetailsActivity.this).load(posterURL).into(detailsActivityImageView);
        }
    };
}
