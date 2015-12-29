package com.test.materialdesigndemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.*;
import android.support.v4.app.*;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.*;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainFragment.MainFragmentInterface, SecondFragment.SecondFragmentInterface, Constants {



    Toolbar toolbar;
    FrameLayout frameLayout;
    TabLayout tabLayout;
    ViewPager viewPager;
    DrawerLayout drawerLayout;
    ActionBar actionBar;
    NavigationView navigationView;
    CoordinatorLayout coordinatorLayout;
    FloatingActionButton floatingActionButton;
    MenuItem menuItem;
    android.support.v7.app.ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUIElements();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        toolBarSetup();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                menuItem = item;

                if (isPermissionGranted() && shouldShowRationale()) {
                    drawerLayout.closeDrawers();
                    showSnackBar(null, R.string.permission_rationale_text, -2);
                } else if ((isPermissionGranted()) && !shouldShowRationale()) {
                    requestReadPhoneStatePermission();
                } else {
                    return switchDataSetAsPermissionIsGranted(item);
                }
                return false;
            }
        });

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    private void requestReadPhoneStatePermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_PERMISSION_ID);
    }


    private boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED;
    }

    private boolean shouldShowRationale() {
        return ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_PHONE_STATE);
    }

    private void toolBarSetup() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.permission_rationale_text,R.string.permission_rationale_text);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    private void initializeUIElements() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
    }

    private boolean switchDataSetAsPermissionIsGranted(MenuItem item) {
        item.setChecked(true);
        drawerLayout.closeDrawers();
        showSnackBar(item, R.string.update_episode_list, -1);
        Intent intent = new Intent(NAV_DRAWER_BROADCAST_RECEIVER);
        String season = String.valueOf(item.getTitle().charAt(item.getTitle().length() - 1));
        intent.putExtra(getString(R.string.season), season);
        LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intent);
        return true;
    }

    private void showSnackBar(MenuItem item, int message, int snackBarLength) {
        Snackbar snackBar;
        if (item == null) {
            snackBar = Snackbar.make(coordinatorLayout, getString(message), snackBarLength).
                    setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestReadPhoneStatePermission();
                        }
                    });


        } else {
            snackBar = Snackbar.make(coordinatorLayout, getString(message) + item.getTitle(), snackBarLength);
        }
        View textView = snackBar.getView();
        TextView tv = (TextView) textView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(getResources().getColor(R.color.snackBarTextColor));
        snackBar.show();
    }

    @Override
    public void listItemClicked(int position, View view, String title, String season) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("episode", Integer.toString(position));
        intent.putExtra("title", title);
        intent.putExtra("season", season);
        ActivityOptionsCompat transition = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, getString(R.string.activity_image_trans));
        startActivity(intent, transition.toBundle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(MainFragment.newInstance(), getString(R.string.tab_one));
        adapter.addFragment(SecondFragment.newInstance(), getString(R.string.tab_two));
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_PHONE_STATE_PERMISSION_ID) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                switchDataSetAsPermissionIsGranted(menuItem);
            } else if (shouldShowRationale()) {
                drawerLayout.closeDrawers();

            } else {
                drawerLayout.closeDrawers();
                Snackbar.make(coordinatorLayout, "Never ask again", Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}



