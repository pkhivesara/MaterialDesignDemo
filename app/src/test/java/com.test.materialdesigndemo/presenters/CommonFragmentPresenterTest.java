package com.test.materialdesigndemo.presenters;

import com.test.materialdesigndemo.BuildConfig;
import com.test.materialdesigndemo.model.EpisodeList;
import com.test.materialdesigndemo.model.IndividualEpisodeResponseEvent;
import junit.framework.Assert;
import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class CommonFragmentPresenterTest {


    @Mock
    CommonFragmentPresenter.MainFragmentPresenterInterface mainFragmentPresenterInterface;

    CommonFragmentPresenter commonFragmentPresenter;

    @Captor
    private ArgumentCaptor<ArrayList<EpisodeList.Episodes>> captor;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        commonFragmentPresenter = new CommonFragmentPresenter(mainFragmentPresenterInterface);

    }


    @Test
    public void testThatPresenterIsNotNull() {
        Assert.assertNotNull(mainFragmentPresenterInterface);
        assertNotNull(commonFragmentPresenter);

    }


    @Test
    public void testThatEventBusIsReceivedOnASuccessfulPostWithTheCorrectObject() throws Exception {
        commonFragmentPresenter.onStart();

        List<EpisodeList.Episodes> testEpisodeList = new ArrayList();
        EpisodeList episodeList = new EpisodeList();
        EpisodeList.Episodes testSingleEpisode = episodeList.new Episodes();
        testSingleEpisode.Episode = "1";
        testSingleEpisode.Title = "Test title";

        testEpisodeList.add(testSingleEpisode);
        IndividualEpisodeResponseEvent individualEpisodeResponseEvent = new IndividualEpisodeResponseEvent(testEpisodeList);
        EventBus.getDefault().post(individualEpisodeResponseEvent);

        commonFragmentPresenter.onStop();

        verify(mainFragmentPresenterInterface).setDataForRecyclerViewAdapter(anyList());
        verify(mainFragmentPresenterInterface).setDataForRecyclerViewAdapter(captor.capture());

        assertThat("1", equalTo(captor.getValue().get(0).getEpisode()));
        assertThat("Test title", equalTo(captor.getValue().get(0).getTitle()));

    }


    @Test
    public void testThatEventBusIsNotReceivedWithAnInCorrectObject() throws Exception {
        commonFragmentPresenter.onStart();

        EventBus.getDefault().post("Invalid String to be send as a message");

        commonFragmentPresenter.onStop();
        verify(mainFragmentPresenterInterface,never()).setDataForRecyclerViewAdapter(anyList());


    }


}
