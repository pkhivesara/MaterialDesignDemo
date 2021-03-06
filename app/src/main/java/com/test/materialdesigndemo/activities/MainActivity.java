package com.test.materialdesigndemo.activities;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.*;
import android.support.v4.app.*;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.*;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.test.materialdesigndemo.Constants;
import com.test.materialdesigndemo.R;
import com.test.materialdesigndemo.dagger.DaggerRetrofitComponent;
import com.test.materialdesigndemo.dagger.RetrofitComponent;
import com.test.materialdesigndemo.dagger.RetrofitModule;
import com.test.materialdesigndemo.fragments.MainFragment;
import com.test.materialdesigndemo.fragments.SecondFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements MainFragment.MainFragmentInterface, SecondFragment.SecondFragmentInterface, Constants {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;

    @Bind(R.id.view_pager)
    ViewPager viewPager;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.navigation_view)
    NavigationView navigationView;

    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    public static RetrofitComponent retrofitComponent;
    public static String URL = "http://www.omdbapi.com/";


    MenuItem menuItem;
    android.support.v7.app.ActionBarDrawerToggle actionBarDrawerToggle;
    ActionBar actionBar;


    @OnClick(R.id.fab)
    public void onClick() {
        int[] array = new int[]{11, 12, 13, 14};
        int number = getRandom(array);
        showNotification(number);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        retrofitComponent = DaggerRetrofitComponent.builder().retrofitModule(new RetrofitModule()).build();
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        getWindow().setExitTransition(new Explode());
        getWindow().setReenterTransition(new Slide(Gravity.LEFT));
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
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

        Toast.makeText(this, resolveIntent(getIntent()), Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
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
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.permission_rationale_text, R.string.permission_rationale_text);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }


    private static int getRandom(int[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }


    private boolean switchDataSetAsPermissionIsGranted(MenuItem item) {
        item.setChecked(true);
        drawerLayout.closeDrawers();
        showSnackBar(item, R.string.update_episode_list, -1);
        Intent intent = new Intent(NAV_DRAWER_BROADCAST_RECEIVER);
        String season = String.valueOf(item.getTitle().charAt(item.getTitle().length() - 1));
        intent.putExtra(getString(R.string.season_four), season);
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
        textView.setBackgroundColor(Color.WHITE);
        TextView tv = (TextView) textView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(getResources().getColor(R.color.snackBarTextColor));

        snackBar.show();
    }

    @Override
    public void listItemClicked(int position, View view, String title, String season) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(getString(R.string.episode), Integer.toString(position));
        intent.putExtra(getString(R.string.title), title);
        intent.putExtra(getString(R.string.season), season);
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


    public void showNotification(int number) {
        NotificationManager notificationManagerCompat = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        switch (number) {
            case TAG_BIG_TEXT_STYLE_NOTIFICATION:

                NotificationCompat.Builder bigTextStyleNotification = showNormalNotification();
                NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle(bigTextStyleNotification);
                notificationManagerCompat.notify(1,
                        bigTextStyle.setSummaryText("Summary Text for Big Text Style")
                                .setBigContentTitle("Big Content Title goes here")
                                .bigText("Some really big content description which is absolutely out of the way necessary should go here. Maybe")
                                .build());
                break;
            case TAG_BIG_PICTURE_STYLE_NOTIFICATION:
                NotificationCompat.Builder bigPictureStyleNotification = showNormalNotification();
                NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle(bigPictureStyleNotification);
                notificationManagerCompat.notify(2,
                        bigPictureStyle.setSummaryText("Summary Text for Big Picture Style")
                                .setBigContentTitle("Big Picture Title goes here")
                                .bigLargeIcon(BitmapFactory.decodeResource(Resources.getSystem(), android.R.drawable.sym_def_app_icon))
                                .build());

                break;
            case TAG_INBOX_STYLE_NOTIFICATION:
                NotificationCompat.Builder inboxStyleNotification = showNormalNotification();
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle(inboxStyleNotification);
                notificationManagerCompat.notify(3,
                        inboxStyle.setSummaryText("Summary Text for Inbox Style")
                                .addLine("First Line")
                                .addLine("Second Line").build());
                break;

            case TAG_STACKED_STYLE_NOTIFICATION:
                NotificationCompat.Builder stackedStyleNotification = showStackedNotification("First notification to be stacked");
                notificationManagerCompat.notify(1, stackedStyleNotification.build());

                NotificationCompat.Builder anotherStackedStyleNotification = showStackedNotification("Second notification to be stacked");
                notificationManagerCompat.notify(2, anotherStackedStyleNotification.build());

                NotificationCompat.Builder stackedNotificationHolder = showNormalNotification();
                stackedNotificationHolder.setGroup("SAMPLE");
                stackedNotificationHolder.setGroupSummary(true);
                NotificationCompat.InboxStyle stackedStyle = new NotificationCompat.InboxStyle(stackedNotificationHolder);
                notificationManagerCompat.notify(3,
                        stackedStyle.setSummaryText("Summary Text for Inbox Style")
                                .addLine("First notification to be stacked")
                                .addLine("Second notification to be stacked").build());
        }
    }


    private android.support.v4.app.NotificationCompat.Builder showNormalNotification() {
        final NotificationCompat.Builder notification = new NotificationCompat.Builder(this)

                .setContentTitle("Title for Notification via NormalNotificationBuilder")
                .setContentText("Content for the Notification via NormalNotificationBuilder")
                .setTicker("Ticker Message in status bar")
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_dialog_email))
                .setPriority(Notification.PRIORITY_MAX)
                .addAction(android.R.drawable.ic_media_play, "Show settings", buildPendingIntent(Settings.ACTION_SECURITY_SETTINGS))
                .setDefaults(Notification.DEFAULT_VIBRATE);
        return notification;

    }


    private android.support.v4.app.NotificationCompat.Builder showStackedNotification(String title) {
        final NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText("Content for the Notification via StackedNotificationBuilder")
                .setTicker("Ticker Message in status bar")
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setGroup("SAMPLE")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_dialog_email))
                .setPriority(Notification.PRIORITY_MAX)
                .addAction(android.R.drawable.ic_media_play, "Show settings", buildPendingIntent(Settings.ACTION_SECURITY_SETTINGS))
                .setDefaults(Notification.DEFAULT_VIBRATE);
        return notification;

    }


    private String resolveIntent(Intent intent) {
        if (Intent.ACTION_SEND.equals(intent.getAction()) &&
                "text/plain".equals(intent.getType())) {
            return intent.getStringExtra(Intent.EXTRA_TEXT);
        }
        return null;
    }


    private PendingIntent buildPendingIntent(String action) {
        Intent intent = new Intent(action);
        return PendingIntent.getActivity(this, 0, intent, 0);
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



