package com.test.materialdesigndemo;

import android.content.ComponentName;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.service.chooser.ChooserTarget;
import android.service.chooser.ChooserTargetService;
import com.test.materialdesigndemo.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 512136 on 1/15/2016.
 */
public class ShareService extends ChooserTargetService {
    @Override
    public List<ChooserTarget> onGetChooserTargets(ComponentName componentName, IntentFilter intentFilter) {
        ArrayList<ChooserTarget> resultSet = new ArrayList<ChooserTarget>();
        for(int i = 0; i<6; i++){
            resultSet.add(buildNewTarget(i));
        }
        return resultSet;
    }

        private ChooserTarget buildNewTarget(int i){
        String title = "SampleDirectShare";
            Icon icon = Icon.createWithResource(getApplicationContext(),R.drawable.ic_himym_1);
        float score = 1f;
        ComponentName componentName1 = new ComponentName(this,MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("ID",1);
        return new ChooserTarget(title,icon,score,componentName1,bundle);
    }
    }

