package com.test.materialdesigndemo;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import javax.net.ssl.HttpsURLConnection;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
         recyclerView = (RecyclerView) view.findViewById(R.id.drawerList);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        initializeData();
        return view;

    }
    private List<TestData> testDataList;


    private void initializeData() {
        testDataList = new ArrayList<TestData>();
        testDataList.add(new TestData("ROW 1 - DATASET 1", "ROW 2 - DATASET 1"));
        testDataList.add(new TestData("ROW 1 - DATASET 2", "ROW 2 - DATASET 2"));
        testDataList.add(new TestData("ROW 1 - DATASET 3", "ROW 2 - DATASET 3"));
        MyAdapter myAdapter = new MyAdapter(testDataList);
        recyclerView.setAdapter(myAdapter);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        List<TestData> testDataLiST;

        MyAdapter(List<TestData> testDataList){
            this.testDataLiST = testDataList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_first, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return testDataLiST.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            public ViewHolder(View itemView) {
                super(itemView);


            }
        }
    }

    private class GetAnotherSampleResponse extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            String response = makeServiceCall();
            try{
                JSONObject jsonObject = new JSONObject(response);
                JSONObject mrData = jsonObject.getJSONObject("MRData");
                String url = mrData.getString("url");
                Log.d("###",url);
                JSONObject raceTable = mrData.getJSONObject("RaceTable");
                String season = raceTable.getString("season");
                String driverId = raceTable.getString("driverId");
                Log.d("!!!",season + "   " + driverId);
                JSONArray raceArray = raceTable.getJSONArray("Races");
                for(int i = 0;i<raceArray.length();i++){
                        JSONObject jsonObject1  =raceArray.getJSONObject(i);
                    String round  = jsonObject1.getString("round");
                    String raceName = jsonObject1.getString("raceName");
                    Log.d("$$$",round + "   " + raceName);
                    JSONObject circuit = jsonObject1.getJSONObject("Circuit");
                    JSONObject location = circuit.getJSONObject("Location");
                    String country = location.getString("country");
                    Log.d("%%%", country);
                    JSONArray quali = jsonObject1.getJSONArray("QualifyingResults");

                    for(int j = 0; j<quali.length();j++){
                        JSONObject innerObject = quali.getJSONObject(j);
                        String position = innerObject.getString("position");
                        String Q1 = innerObject.getString("Q1");
                        Log.d("***",position + "    " + Q1);
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class GetSampleResponse extends AsyncTask<String,String,String>{



        @Override
        protected String doInBackground(String... params) {
            String response = makeServiceCall();
            try {
               JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonObject1 = jsonObject.getJSONObject("MRData");
                String total = jsonObject1.getString("total");
                Log.d("@@@",total);
                JSONObject table = jsonObject1.getJSONObject("RaceTable");
                String season = table.getString("season");
                Log.d("@@@",season);
                JSONArray jsonArray = table.getJSONArray("Races");

                for(int i = 0;i< jsonArray.length(); i ++){
                    JSONObject innerObject  = jsonArray.getJSONObject(i);
                    String id = innerObject.getString("raceName");
                    Log.d("@@@",id);
                    JSONObject name = innerObject.getJSONObject("Circuit");
                    String circuitName = name.getString("circuitName");
                    String date = innerObject.getString("date");
                    Log.d("###",circuitName + date);
                    JSONArray laps = innerObject.getJSONArray("Laps");

                    for(int j = 0;j< laps.length();j++){
                        JSONObject jsonObject2 = laps.getJSONObject(j);
                        String number = jsonObject2.getString("number");
                        Log.d("$$$",number);
                        JSONArray timings = jsonObject2.getJSONArray("Timings");
                        for(int k = 0;k<timings.length();k++){
                            JSONObject jsonObject3 = timings.getJSONObject(k);
                            String driverName = jsonObject3.getString("driverId");
                            Log.d("%%%",driverName);
                        }
                    }

                Log.d("^^^",id + name);
                }

                Log.d("***",total);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("######", e.getMessage());
            }
            return null;
        }
    }


    private String makeServiceCall(){
        StringBuffer response = null;
        URL url = null;
        try {
            url = new URL("http://ergast.com/api/f1/2008/drivers/alonso/qualifying.json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection httpsURLConnection = (HttpURLConnection)url.openConnection();
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
