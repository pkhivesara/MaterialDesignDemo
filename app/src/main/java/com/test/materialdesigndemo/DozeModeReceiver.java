package com.test.materialdesigndemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class DozeModeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Works!",Toast.LENGTH_SHORT).show();
        Log.d("^^^","doe mode works");

    }
}
