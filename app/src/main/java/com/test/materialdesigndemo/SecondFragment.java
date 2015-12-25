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

/**
 * Created by Pratik on 12/24/15.
 */
public class SecondFragment extends Fragment {
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.drawerList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerDecoration(getActivity()));
        new GetSampleResponse().execute();
        return view;
    }

    public static SecondFragment newInstance() {
        return new SecondFragment();
    }


    private class GetSampleResponse extends AsyncTask<Void, Void, List> {


        @Override
        protected List doInBackground(Void... params) {
            String response = makeServiceCall();
            List<String> responseList = new ArrayList<String>();
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonObject1 = jsonObject.getJSONObject("MRData");
                String total = jsonObject1.getString("total");
                Log.d("@@@", total);
                JSONObject constructorTable = jsonObject1.getJSONObject("ConstructorTable");
                String season = constructorTable.getString("season");
                Log.d("@@@", season);
                JSONArray jsonArray = constructorTable.getJSONArray("Constructors");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject innerObject = jsonArray.getJSONObject(i);
                    String constructorId = innerObject.getString("constructorId");
                    Log.d("@@@", constructorId);
                    String constructorName = innerObject.getString("name");
                    Log.d("###", constructorName);
                    String constructorNationality = innerObject.getString("nationality");
                    responseList.add(constructorName);
                }

                Log.d("***", total);

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

    private void initializeData(List<String> responseList) {

        MyAdapter myAdapter = new MyAdapter(responseList);
        recyclerView.setAdapter(myAdapter);
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
                //mainFragmentInterface.listItemClicked(getAdapterPosition());
                Toast.makeText(getActivity(), "Clicked row is:" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String makeServiceCall() {
        StringBuffer response = null;
        URL url = null;
        try {
            url = new URL("http://ergast.com/api/f1/2015/constructors.json");
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
