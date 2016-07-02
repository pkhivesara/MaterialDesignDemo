package com.test.materialdesigndemo.presenters;


import com.test.materialdesigndemo.ServiceHelper;
import com.test.materialdesigndemo.model.EpisodeList;
import com.test.materialdesigndemo.model.IndividualEpisodeResponseEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


import java.util.List;

/**
 * Created by Pratik on 7/1/16.
 */
public class CommonFragmentPresenter {
    MainFragmentPresenterInterface mainFragmentPresenterInterface;
    ServiceHelper serviceHelper;

    public CommonFragmentPresenter(MainFragmentPresenterInterface mainFragmentPresenterInterface){
        this.mainFragmentPresenterInterface = mainFragmentPresenterInterface;
        serviceHelper = new ServiceHelper();

    }

    public void getIndividualEpisodeData(String show, String season) {
        serviceHelper.getIndividualEpisodeData(show, season);
    }

    @Subscribe
    public void handleIndividualEpisodeData(IndividualEpisodeResponseEvent event) {
    mainFragmentPresenterInterface.setDataForRecyclerViewAdapter(event.getEpisodesList());
    }

    public void onStart(){
        EventBus.getDefault().register(this);
    }

    public void onStop(){
        EventBus.getDefault().unregister(this);
    }

    public interface MainFragmentPresenterInterface{
        void setDataForRecyclerViewAdapter(List<EpisodeList.Episodes> episodes);
    }
}
