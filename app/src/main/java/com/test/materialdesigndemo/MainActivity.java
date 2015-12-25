package com.test.materialdesigndemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainFragment.MainFragmentInterface{
    Toolbar toolbar;
    FrameLayout frameLayout;
    TabLayout tabLayout;
    ViewPager viewPager;
    DrawerLayout drawerLayout;
    ActionBar actionBar;
    NavigationView navigationView;
    CoordinatorLayout coordinatorLayout;
FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        frameLayout = (FrameLayout)findViewById(R.id.frame_layout);
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_search);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {


                item.setChecked(true);
                drawerLayout.closeDrawers();
                Snackbar.make(coordinatorLayout,"Updating Race and Constructors List for " + item.getTitle(),Snackbar.LENGTH_LONG).show();

                Intent intent = new Intent("NAV_DRAWER_ITEM_CLICKED");
                intent.putExtra("Year",item.getTitle());
                LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intent);
                return true;
            }
        });

    }

    @Override
    public void listItemClicked(int position) {
//        DetailsFragment detailsFragment = DetailsFragment.newInstance();
//        Bundle bundle = new Bundle();
//        bundle.putInt("position",position);
//        detailsFragment.setArguments(bundle);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
//        transaction.replace(R.id.frame_layout, detailsFragment);
//        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(MainFragment.newInstance(), "Tab One");
        adapter.addFragment(SecondFragment.newInstance(), "Tab Two");
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
