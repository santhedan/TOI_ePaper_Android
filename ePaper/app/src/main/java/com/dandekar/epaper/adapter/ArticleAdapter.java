package com.dandekar.epaper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dandekar.epaper.R;
import com.dandekar.epaper.model.Article;

import java.util.List;

/**
 * Created by sanjay_dandekar on 02/10/14.
 */
public class ArticleAdapter extends BaseAdapter {

    private List<Article> articles = null;

    private LayoutInflater inflater = null;

    public ArticleAdapter (List<Article> articles, Context context) {
        this.articles = articles;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (articles != null) {
            return articles.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (articles != null) {
            return articles.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.article_row, null);
        }
        TextView title = (TextView) view.findViewById(R.id.articleTitle);
        TextView detail = (TextView) view.findViewById(R.id.articleDetail);
        //
        Article art = articles.get(i);
        //
        title.setText(art.getTitle());
        detail.setText(art.getBody());
        //
        return view;
    }
}
