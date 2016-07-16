package com.test.materialdesigndemo.presenters;

import android.content.Context;
import com.test.materialdesigndemo.BuildConfig;
import com.test.materialdesigndemo.ServiceHelper;
import com.test.materialdesigndemo.activities.MainActivity;
import com.test.materialdesigndemo.model.EpisodeList;
import com.test.materialdesigndemo.model.IndividualEpisodeResponseEvent;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.greenrobot.eventbus.EventBus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;


@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ServiceHelperTest {

    private MockWebServer server;
    ServiceHelper serviceHelper;

    @Mock
    EventBus eventBus;

    @Captor
    private ArgumentCaptor<List<EpisodeList.Episodes>> captor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        server = new MockWebServer();
        serviceHelper = new ServiceHelper();

    }

    @Test
    public void testEventBusIsPostedOnSuccessfulServiceCall() throws Exception {
        server.start();
        server.enqueue(new MockResponse().setResponseCode(200).setBody(getStringFromFile(RuntimeEnvironment.application, "episodes_list_success.json")));
        MainActivity.URL = server.url("/").toString();
        serviceHelper.setEventBus(eventBus);
        serviceHelper.getIndividualEpisodeData("Friends", "7");
        verify(eventBus).post(anyObject());
        verify(eventBus).post(captor.capture());
        IndividualEpisodeResponseEvent episodeLists = (IndividualEpisodeResponseEvent) captor.getValue();
        assertThat("The One Where I am testing something",equalTo(episodeLists.getEpisodesList().get(0).getTitle()));
    }

    @Test
    public void testEventBusIsPostedWithNoDataOnServiceCallWithAnIncorrectResponse() throws Exception {
        server.start();
        server.enqueue(new MockResponse().setResponseCode(200).setBody(getStringFromFile(RuntimeEnvironment.application, "incorrect_success_response.json")));
        MainActivity.URL = server.url("/").toString();
        serviceHelper.setEventBus(eventBus);
        serviceHelper.getIndividualEpisodeData("Friends", "7");
        verify(eventBus).post(anyObject());
        verify(eventBus).post(captor.capture());
        IndividualEpisodeResponseEvent episodeLists = (IndividualEpisodeResponseEvent) captor.getValue();
        assertNull(episodeLists.getEpisodesList());
    }

    @Test
    public void testEventBusIsNotPostedOn500Error() throws Exception {
        server.start();
        server.enqueue(new MockResponse().setResponseCode(500).setBody(getStringFromFile(RuntimeEnvironment.application, "incorrect_success_response.json")));
        MainActivity.URL = server.url("/").toString();
        serviceHelper.setEventBus(eventBus);
        serviceHelper.getIndividualEpisodeData("Friends", "7");
        verifyNoMoreInteractions(eventBus);

    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    private String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    private String getStringFromFile(Context context, String filePath) throws Exception {
        final InputStream stream = context.getResources().getAssets().open(filePath);

        String ret = convertStreamToString(stream);
        stream.close();
        return ret;
    }
}
