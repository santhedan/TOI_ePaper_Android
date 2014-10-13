package com.dandekar.epaper;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.dandekar.epaper.adapter.ArticleAdapter;
import com.dandekar.epaper.model.Article;
import com.dandekar.epaper.model.Page;

public class ArticleListActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        getActionBar().setDisplayShowHomeEnabled(false);
        Page page = (Page) getIntent().getSerializableExtra("PAGE");
        // Get articles list and create adapter
        setListAdapter(new ArticleAdapter(page.getArticles(), this));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Article article = (Article) getListAdapter().getItem(position);
        Intent intent = new Intent(this, ArticleViewerActivity.class);
        intent.putExtra("URL", article.getImageUrl());
        intent.putExtra("TITLE", article.getTitle());
        startActivity(intent);
    }
}
