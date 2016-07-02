package com.test.materialdesigndemo.presenters;


import android.content.Context;
import com.test.materialdesigndemo.network.RestClient;
import junit.framework.Assert;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.MockAnnotationProcessor;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Pratik on 7/1/16.
 */

@RunWith(MockitoJUnitRunner.class)
public class CommonFragmentPresenterTest {

//        @Test
//    public void setUp(){
//            assertEquals(4,4);
//        //commonFragmentPresenter = new CommonFragmentPresenter(mainFragmentPresenterInterface);
//    }

    @Mock
    CommonFragmentPresenter.MainFragmentPresenterInterface mainFragmentPresenterInterface;
    CommonFragmentPresenter commonFragmentPresenter;
    MockWebServer mockWebServer;

    @Mock
    Context mockContext;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        commonFragmentPresenter = new CommonFragmentPresenter(mainFragmentPresenterInterface);
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        RestClient.URL = mockWebServer.url("/").toString();



    }


    @Test
    public void testThatPresenterIsNotNull(){
        Assert.assertNotNull(mainFragmentPresenterInterface);
        assertNotNull(commonFragmentPresenter);

    }

    @Test
    public void testThatEventBusIsReceivedOnMakingServiceCall() throws Exception {
        System.out.println(CommonFragmentPresenterTest.class.getResource(".").getPath());
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(getStringFromFile("episodes_list_success.json")));
        commonFragmentPresenter.getIndividualEpisodeData("Friends","1");
        verify(mainFragmentPresenterInterface).setDataForRecyclerViewAdapter(anyList());
    }


    private  String getStringFromFile(String filePath) throws Exception {

        final InputStream stream =getClass().getClassLoader().getResourceAsStream(filePath);
        String ret = convertStreamToString(stream);
        stream.close();
        return ret;
    }

    public  String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }
}
