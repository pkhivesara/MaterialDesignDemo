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

public class FirstFragment extends Fragment {
    RecyclerView recyclerView;
    Button button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
         recyclerView = (RecyclerView) view.findViewById(R.id.drawerList);
        button = (Button)view.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetSampleResponse().execute();
            }
        });
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        initializeData();
        return view;

    }
    private List<TestData> testDataList;

    // This method creates an ArrayList that has three Person objects
// Checkout the project associated with this tutorial on Github if
// you want to use the same images.
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
            holder.rowOne.setText(testDataLiST.get(position).firstRow);
            holder.rowTwo.setText(testDataLiST.get(position).secondRow);
        }

        @Override
        public int getItemCount() {
            return testDataLiST.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView rowOne;
            TextView rowTwo;
            public ViewHolder(View itemView) {
                super(itemView);
                rowOne = (TextView)itemView.findViewById(R.id.row_one);
                rowTwo = (TextView)itemView.findViewById(R.id.row_two);

            }
        }
    }

    private class GetSampleResponse extends AsyncTask<String,String,String>{



        @Override
        protected String doInBackground(String... params) {
            String response = makeServiceCall();
            try {
               JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonObject1 = jsonObject.getJSONObject("MRData");
                String url = jsonObject1.getString("url");
                JSONObject table = jsonObject1.getJSONObject("DriverTable");
                JSONArray jsonArray = table.getJSONArray("Drivers");

                for(int i = 0;i< jsonArray.length(); i ++){
                    JSONObject innerObject  = jsonArray.getJSONObject(i);
                    String id = innerObject.getString("driverId");
                    String name = innerObject.getString("familyName");
                Log.d("@@@",id + name);
                }

                Log.d("@@@",url);

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
            url = new URL("http://ergast.com/api/f1/2011/drivers.json");
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
