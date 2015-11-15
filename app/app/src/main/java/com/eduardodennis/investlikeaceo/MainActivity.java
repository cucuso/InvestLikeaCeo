package com.eduardodennis.investlikeaceo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import investlikeceo.eduardodennis.com.investlikeaceo.R;

import com.eduardodennis.investlikeaceo.data.Stock;
import com.eduardodennis.investlikeaceo.data.StockDataIntentService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private ResponseReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView txtError = (TextView) this.findViewById(R.id.error);

        recViewInit();

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.bringToFront();


        if (!isNetworkConnected()) {

            txtError.setText("Please try again when you are connected to the internet!");

        } else {

            IntentFilter broadcastFilter = new IntentFilter(ResponseReceiver.LOCAL_ACTION);
            receiver = new ResponseReceiver();
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
            localBroadcastManager.registerReceiver(receiver, broadcastFilter);

            //new StockDataGenerator(this).execute(getResources().getString(R.string.url));
            Intent msgIntent = new Intent(MainActivity.this, StockDataIntentService.class);
            msgIntent.putExtra(StockDataIntentService.PARAM_IN_MSG, getResources().getString(R.string.url));
            startService(msgIntent);

        }
    }



    private void recViewInit() {
        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.unregisterReceiver(receiver);

    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

    public class ResponseReceiver extends BroadcastReceiver {

        public static final String LOCAL_ACTION = "com.eduardodennis.investlikeaceo.ALL_DONE";
        private Handler handler;


        @Override
        public void onReceive(Context context, Intent intent) {

            String text = intent.getStringExtra(StockDataIntentService.PARAM_OUT_MSG);

            TextView txtError = (TextView) findViewById(R.id.error);

            if ("".equals(text) || text.equals("Did not work!")||"{}".equals(text)) {

                txtError.setText("Error retrieving stock data from server!");

            } else {

                List<Stock> stockList = getStocks(text);
                addStockListToUi(stockList, context);


            }

            Log.d("receiver", text);

        }

       private void addStockListToUi(List<Stock> stockList, Context context) {

            RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
            recList.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(context);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recList.setLayoutManager(llm);

            StockAdapter ca = new StockAdapter(stockList);
            recList.setAdapter(ca);
        }

        private List<Stock> getStocks(String result) {


            List<Stock> stockList = new ArrayList<>();
            String[] stocks = result.split(",");

            Long totalAmount = 0L;


            for (int i = 0; i < stocks.length; i++) {
                Stock fStock = new Stock();
                String[] stockData = stocks[i].split(":");
                if (i % 2 == 0) {
                    totalAmount += Long.parseLong(stockData[2]);
                    fStock.setSymbol(stockData[0].replace("\"", "").replace("{", ""));
                    fStock.setPrice(Long.parseLong(stockData[2]));
                    stockList.add(fStock);
                }
            }

            for (Stock stock : stockList) {

                stock.setWeight((double) stock.getPrice() / totalAmount);
            }

            Collections.sort(stockList, new Comparator<Stock>() {
                @Override
                public int compare(Stock lhs, Stock rhs) {
                    return (int) (rhs.getPrice() - lhs.getPrice());
                }
            });

            return stockList;
        }

    }

}

