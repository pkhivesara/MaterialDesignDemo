package com.test.materialdesigndemo.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.test.materialdesigndemo.*;
import com.test.materialdesigndemo.model.EpisodeList;
import com.test.materialdesigndemo.model.IndividualEpisodeData;
import com.test.materialdesigndemo.network.RestClient;
import com.test.materialdesigndemo.presenters.CommonFragmentPresenter;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import java.util.List;


public class SecondFragment extends Fragment implements Constants, CommonFragmentPresenter.MainFragmentPresenterInterface {
    RecyclerView recyclerView;
    SecondFragmentInterface secondFragmentInterface;
    String season;
    AlarmManager alarmManager;
    CommonFragmentPresenter commonFragmentPresenter;
    AlarmManager.AlarmClockInfo alarmClockInfo;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.drawerList);
        intent = new Intent(getActivity(), DozeModeReceiver.class);
        commonFragmentPresenter = new CommonFragmentPresenter(this);
        PendingIntent dozeModePendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmClockInfo = new AlarmManager.AlarmClockInfo(System.currentTimeMillis() + 30000, dozeModePendingIntent);
        //  alarmManager.setAlarmClock(alarmClockInfo,dozeModePendingIntent);
        // alarmManager.setExactAndAllowWhileIdle(1,30000,dozeModePendingIntent);

        setUpRecyclerView();
        commonFragmentPresenter.getIndividualEpisodeData(getString(R.string.friends_title), "1");
        return view;
    }


    private void setUpRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerDecoration(getActivity()));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        secondFragmentInterface = (SecondFragmentInterface) activity;

    }

    public static SecondFragment newInstance() {
        return new SecondFragment();
    }


    BroadcastReceiver navDrawerClickedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            season = intent.getStringExtra(context.getString(R.string.season_four));
            commonFragmentPresenter.getIndividualEpisodeData(getString(R.string.friends_title), season);
        }
    };


    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(navDrawerClickedReceiver, new IntentFilter(NAV_DRAWER_BROADCAST_RECEIVER));
        commonFragmentPresenter.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(navDrawerClickedReceiver);
        commonFragmentPresenter.onStop();
    }

    @Override
    public void setDataForRecyclerViewAdapter(List<EpisodeList.Episodes> episodes) {
        MyAdapter myAdapter = new MyAdapter(episodes);
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void setIndividualEpisodeDetails(IndividualEpisodeData individualEpisodeData) {

    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MainViewHolder> {

        List<EpisodeList.Episodes> responseList;

        MyAdapter(List<EpisodeList.Episodes> testDataList) {
            this.responseList = testDataList;
        }

        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case TYPE_LIST:
                    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
                    return new ListViewHolder(v);
                case TYPE_HEADER:
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_row, parent, false);
                    return new HeaderViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case TYPE_LIST:
                    String raceName = responseList.get(position - 1).Title;
                    ListViewHolder listViewHolder = (ListViewHolder) holder;
                    listViewHolder.raceNameTextView.setText(raceName);
                    listViewHolder.thumbnailImageView.setImageResource(R.drawable.ic_friends);
                    break;
                case TYPE_HEADER:
                    HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                    headerViewHolder.headerTextView.setText(R.string.friends_title);
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_HEADER;
            } else {
                return TYPE_LIST;
            }
        }

        @Override
        public int getItemCount() {
            return responseList.size() + 1;
        }


        public class HeaderViewHolder extends MainViewHolder {
            @Bind(R.id.header_text_view)
            TextView headerTextView;

            public HeaderViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        public class MainViewHolder extends RecyclerView.ViewHolder {

            public MainViewHolder(View itemView) {
                super(itemView);
            }
        }

        public class ListViewHolder extends MainViewHolder implements View.OnClickListener {
            @Bind(R.id.raceName)
            TextView raceNameTextView;

            @Bind(R.id.thumbNailImageView)
            ImageView thumbnailImageView;


            public ListViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                ButterKnife.bind(this, itemView);

            }

            @Override
            public void onClick(View v) {
                if (season == null) {
                    season = "1";
                }
                secondFragmentInterface.listItemClicked(getAdapterPosition(), v.findViewById(R.id.thumbNailImageView), getString(R.string.friends_title), season);
            }
        }
    }


    public interface SecondFragmentInterface {
        void listItemClicked(int position, View view, String title, String season);
    }
}
