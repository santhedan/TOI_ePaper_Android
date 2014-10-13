package com.dandekar.epaper;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.dandekar.epaper.R;

public class ArticleViewerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_viewer);
        getActionBar().setDisplayShowHomeEnabled(false);
        String url = getIntent().getStringExtra("URL");
        String title = getIntent().getStringExtra("TITLE");
        setTitle(title);
        WebView wv = (WebView) findViewById(R.id.webView);
        wv.loadUrl(url);
    }

}
