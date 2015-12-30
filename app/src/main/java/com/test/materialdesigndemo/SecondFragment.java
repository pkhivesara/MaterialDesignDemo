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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class SecondFragment extends Fragment implements Constants {
    RecyclerView recyclerView;
    SecondFragmentInterface secondFragmentInterface;
    String season;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.drawerList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerDecoration(getActivity()));
        new GetConstructorsList().execute(getString(R.string.season_one));
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        secondFragmentInterface = (SecondFragmentInterface) activity;

    }

    public static SecondFragment newInstance() {
        return new SecondFragment();
    }


    private class GetConstructorsList extends AsyncTask<String, Void, List> {


        @Override
        protected List doInBackground(String... params) {


            String year = params[0];
            String response = makeServiceCall(year);
            List<String> responseList = new ArrayList<String>();


            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray searchArray = jsonObject.getJSONArray("Episodes");

                for (int i = 0; i < searchArray.length(); i++) {
                    JSONObject mobileObject = searchArray.getJSONObject(i);
                    String movieTitle = mobileObject.getString("Title");
                    responseList.add(movieTitle);
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("######", e.getMessage());
            }
            return responseList;
        }

        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            initializeData(list);

        }

    }

    BroadcastReceiver navDrawerClickedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String year = intent.getStringExtra(context.getString(R.string.season));
            season = year;
            new GetConstructorsList().execute(year);
        }
    };


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


    private void initializeData(List<String> responseList) {

        MyAdapter myAdapter = new MyAdapter(responseList);
        recyclerView.setAdapter(myAdapter);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MainViewHolder> {

        List<String> responseList;

        MyAdapter(List<String> testDataList) {
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
                    String raceName = responseList.get(position - 1);
                    ListViewHolder listViewHolder = (ListViewHolder) holder;
                    listViewHolder.raceNameTextView.setText(raceName);
                 //   listViewHolder.thumbnailImageView.setImageResource(R.drawable.ic_himym);
                    break;
                case TYPE_HEADER:
                    HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                    headerViewHolder.headerTextView.setText("Friends");
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
            TextView headerTextView;

            public HeaderViewHolder(View itemView) {
                super(itemView);
                headerTextView = (TextView) itemView.findViewById(R.id.header_text_view);
            }
        }

        public class MainViewHolder extends RecyclerView.ViewHolder {

            public MainViewHolder(View itemView) {
                super(itemView);
            }
        }

        public class ListViewHolder extends MainViewHolder implements View.OnClickListener {
            TextView raceNameTextView;
            ImageView thumbnailImageView;


            public ListViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                raceNameTextView = (TextView) itemView.findViewById(R.id.raceName);
                thumbnailImageView = (ImageView) itemView.findViewById(R.id.thumbNailImageView);

            }

            @Override
            public void onClick(View v) {
                secondFragmentInterface.listItemClicked(getAdapterPosition(), v.findViewById(R.id.thumbNailImageView), "Friends",season);
            }
        }
    }

    private String makeServiceCall(String year) {
        StringBuffer response = null;
        URL url = null;
        try {
            url = new URL("http://www.omdbapi.com/?t=Friends&Season=" + year);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection httpsURLConnection = (HttpURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpsURLConnection.getInputStream()));
            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public interface SecondFragmentInterface {
        void listItemClicked(int position, View view, String title,String season);
    }
}
