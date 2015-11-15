package com.eduardodennis.investlikeaceo.data;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.eduardodennis.investlikeaceo.MainActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Eddie on 4/25/2015.
 */
public class StockDataIntentService extends IntentService {
    public static final String PARAM_IN_MSG = "imsg";
    public static final String PARAM_OUT_MSG = "omsg";


    public StockDataIntentService() {
        super("StockDataIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("StockDataIntent", "you made it to intent");

        String url = intent.getStringExtra(PARAM_IN_MSG);


         String stockData =  POST(url);


        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MainActivity.ResponseReceiver.LOCAL_ACTION);
        broadcastIntent.putExtra(PARAM_OUT_MSG, stockData);

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(broadcastIntent);

    }

    private  String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            Log.d("Network request", "HTTP request: "+url);

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make Post request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpPost(url));


            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

}
