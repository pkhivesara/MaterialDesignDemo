package com.test.materialdesigndemo.activities;

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
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.test.materialdesigndemo.R;
import com.test.materialdesigndemo.fragments.DetailsFragment;


public class DetailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView detailsActivityImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Slide(Gravity.RIGHT));
        getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.image_transform));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        postponeEnterTransition();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        detailsActivityImageView = (ImageView) findViewById(R.id.detailsActivityImageView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.details_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final DetailsFragment detailsFragment = DetailsFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.episode), getIntent().getStringExtra(getString(R.string.episode)));
        bundle.putString(getString(R.string.season), getIntent().getStringExtra(getString(R.string.season)));
        bundle.putString(getString(R.string.title), getIntent().getStringExtra(getString(R.string.title)));
        detailsFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.container_layout, detailsFragment);
        fragmentTransaction.commit();

        detailsActivityImageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                detailsActivityImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                startPostponedEnterTransition();
                return true;
            }
        });

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
        LocalBroadcastManager.getInstance(this).registerReceiver(loadCompleteBroadcastReceiver,new IntentFilter(getString(R.string.load_complete_broadcast)));
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
            startPostponedEnterTransition();
        }
    };
}
