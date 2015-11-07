package com.test.materialdesigndemo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
         recyclerView = (RecyclerView) view.findViewById(R.id.drawerList);
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
}
