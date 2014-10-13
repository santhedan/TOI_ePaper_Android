package com.dandekar.epaper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dandekar.epaper.R;
import com.dandekar.epaper.model.Page;

import java.util.List;

/**
 * Created by sanjay_dandekar on 02/10/14.
 */
public class PageAdapter extends BaseAdapter {

    private List<Page> pages = null;

    private LayoutInflater inflater = null;

    public  PageAdapter (List<Page> pages, Context context) {
        this.pages = pages;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (pages != null) {
            return pages.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (pages != null) {
            return pages.get(i);
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
            view = inflater.inflate(R.layout.page_cell, null);
        }
        Page p = pages.get(i);
        ImageView iv = (ImageView) view.findViewById(R.id.pageThumb);
        iv.setImageBitmap(p.getThumbnailImage());
        return view;
    }
}
