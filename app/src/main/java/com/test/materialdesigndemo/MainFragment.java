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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
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

public class MainFragment extends Fragment {
    RecyclerView recyclerView;
    MainFragmentInterface mainFragmentInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.drawerList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerDecoration(getActivity()));
        new GetAnotherSampleResponse().execute("2015");
        return view;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainFragmentInterface = (MainFragmentInterface) activity;

    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(navDrawerClickedReceiver,new IntentFilter("NAV_DRAWER_ITEM_CLICKED"));


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

    public interface MainFragmentInterface {
        void listItemClicked(int position);
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        List<String> responseList;

        MyAdapter(List<String> testDataList) {
            this.responseList = testDataList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String raceName = responseList.get(position);
            holder.raceNameTextView.setText(raceName);
        }

        @Override
        public int getItemCount() {
            return responseList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView raceNameTextView;


            public ViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                raceNameTextView = (TextView) itemView.findViewById(R.id.raceName);

            }

            @Override
            public void onClick(View v) {
                mainFragmentInterface.listItemClicked(getAdapterPosition());
                Toast.makeText(getActivity(), "Clicked row is:" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    BroadcastReceiver navDrawerClickedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String year = intent.getStringExtra("Year");
            new GetAnotherSampleResponse().execute(year);
        }
    };

    private class GetAnotherSampleResponse extends AsyncTask<String, Void, List> {

        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            initializeData(list);

        }

        @Override
        protected List doInBackground(String... params) {
            String year = params[0];
            String response = makeServiceCall(year);
            List<String> responseList = new ArrayList<String>();
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject mrData = jsonObject.getJSONObject("MRData");
                String url = mrData.getString("url");
                JSONObject raceTable = mrData.getJSONObject("RaceTable");
                String season = raceTable.getString("season");
                String driverId = raceTable.getString("driverId");
                JSONArray raceArray = raceTable.getJSONArray("Races");
                for (int i = 0; i < raceArray.length(); i++) {
                    JSONObject jsonObject1 = raceArray.getJSONObject(i);
                    String round = jsonObject1.getString("round");
                    String raceName = jsonObject1.getString("raceName");
                    responseList.add(raceName);
                    JSONObject circuit = jsonObject1.getJSONObject("Circuit");
                    JSONObject location = circuit.getJSONObject("Location");
                    JSONArray quali = jsonObject1.getJSONArray("QualifyingResults");

                    for (int j = 0; j < quali.length(); j++) {
                        JSONObject innerObject = quali.getJSONObject(j);
                        String position = innerObject.getString("position");
                        String Q1 = innerObject.getString("Q1");
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return responseList;
        }
    }




    private String makeServiceCall(String year) {
        StringBuffer response = null;
        URL url = null;
        try {
            url = new URL("http://ergast.com/api/f1/"+year+"/drivers/alonso/qualifying.json");
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
}
