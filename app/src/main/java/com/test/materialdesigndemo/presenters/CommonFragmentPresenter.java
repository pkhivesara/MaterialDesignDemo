package com.test.materialdesigndemo.presenters;


import com.test.materialdesigndemo.ServiceHelper;
import com.test.materialdesigndemo.model.EpisodeList;
import com.test.materialdesigndemo.model.IndividualEpisodeData;
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

    public CommonFragmentPresenter(MainFragmentPresenterInterface mainFragmentPresenterInterface) {
        this.mainFragmentPresenterInterface = mainFragmentPresenterInterface;
        serviceHelper = new ServiceHelper();

    }

    public void getIndividualEpisodeData(String show, String season) {
        serviceHelper.getIndividualEpisodeData(show, season);
    }

    public void getEpisodeDetails(String title, String season, String episode) {
        serviceHelper.getEpisodeDetails(title, season, episode);
    }

    @Subscribe
    public void handleIndividualEpisodeData(IndividualEpisodeResponseEvent event) {
        if (event.getEpisodesList() != null) {
            mainFragmentPresenterInterface.setDataForRecyclerViewAdapter(event.getEpisodesList());
        } else {
            mainFragmentPresenterInterface.setIndividualEpisodeDetails(event.getIndividualEpisodeData());
        }
    }


    public void onStart() {
        EventBus.getDefault().register(this);
    }

    public void onStop() {
        EventBus.getDefault().unregister(this);
    }

    public interface MainFragmentPresenterInterface {
        void setDataForRecyclerViewAdapter(List<EpisodeList.Episodes> episodes);

        void setIndividualEpisodeDetails(IndividualEpisodeData individualEpisodeData);
    }
}
