package com.test.materialdesigndemo.presenters;

import android.app.Service;
import android.content.Context;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.test.materialdesigndemo.BuildConfig;
import com.test.materialdesigndemo.Constants;
import com.test.materialdesigndemo.ServiceHelper;
import com.test.materialdesigndemo.activities.MainActivity;
import com.test.materialdesigndemo.network.ApiCall;
import com.test.materialdesigndemo.network.RestClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;


@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ServiceHelperTest {

    private MockWebServer server;
    ServiceHelper serviceHelper = new ServiceHelper();

    @Mock
    EventBus eventBus;
    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        server = new MockWebServer();



    }

    @Test
    public void testMessageThrown() throws Exception {
        server.start();
        server.enqueue(new MockResponse().setResponseCode(200).setBody(getStringFromFile(RuntimeEnvironment.application, "episodes_list_success.json")));
        MainActivity.URL = server.url("/").toString();

        serviceHelper.getIndividualEpisodeData("Friends","7");
        verify(eventBus).post(any());
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile(Context context, String filePath) throws Exception {
        final InputStream stream = context.getResources().getAssets().open(filePath);

        String ret = convertStreamToString(stream);
        //Make sure you close all streams.
        stream.close();
        return ret;
    }
}
