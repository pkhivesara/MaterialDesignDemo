package com.test.materialdesigndemo;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
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
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements Constants {
    RecyclerView recyclerView;
    MainFragmentInterface mainFragmentInterface;
    String season;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.drawerList);
        setUpRecyclerView();
        Call<EpisodeList> episodeDataList = RestClient.get().getEpisodeList("How I Met Your Mother", "1");
        episodeDataList.enqueue(new Callback<EpisodeList>() {
            @Override
            public void onResponse(Response<EpisodeList> response, Retrofit retrofit) {

                initializeData(response.body().Episodes);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("%%%%%", "fail");
            }
        });
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
        mainFragmentInterface = (MainFragmentInterface) activity;

    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(navDrawerClickedReceiver, new IntentFilter(NAV_DRAWER_BROADCAST_RECEIVER));


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(navDrawerClickedReceiver);


    }

    private void initializeData(List<EpisodeList.Episodes> responseList) {

        MyAdapter myAdapter = new MyAdapter(responseList);
        recyclerView.setAdapter(myAdapter);
    }

    public interface MainFragmentInterface {
        void listItemClicked(int position, View view, String title, String season);
    }

    public static MainFragment newInstance() {
        return new MainFragment();
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
                    Picasso.with(getActivity()).load(R.drawable.ic_himym_1).into(listViewHolder.thumbNailImageView);
                    break;
                case TYPE_HEADER:
                    HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                    headerViewHolder.headerTextView.setText(R.string.himym_title);

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
            ImageView thumbNailImageView;


            public ListViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(this);

            }

            @Override
            public void onClick(View v) {
                mainFragmentInterface.listItemClicked(getAdapterPosition(), v.findViewById(R.id.thumbNailImageView), "How", season);
            }
        }
    }


    BroadcastReceiver navDrawerClickedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String year = intent.getStringExtra(context.getString(R.string.season_four));
            season = year;
            Call<EpisodeList> episodeDataList = RestClient.get().getEpisodeList("How I Met Your Mother", season);
            episodeDataList.enqueue(new Callback<EpisodeList>() {
                @Override
                public void onResponse(Response<EpisodeList> response, Retrofit retrofit) {

                    initializeData(response.body().Episodes);
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("%%%%%", "fail");
                }
            });
        }
    };


}
