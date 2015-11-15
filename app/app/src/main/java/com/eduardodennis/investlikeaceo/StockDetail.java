package com.eduardodennis.investlikeaceo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import investlikeceo.eduardodennis.com.investlikeaceo.R;

public class StockDetail extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String id = intent.getStringExtra("ticker");


        WebView webview = new WebView(this);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl("http://finance.yahoo.com/q?s=" + id);
        setContentView(webview);


    }
}

