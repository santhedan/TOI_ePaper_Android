package com.dandekar.epaper.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dandekar.epaper.R;
import com.dandekar.epaper.model.Edition;

import java.util.ArrayList;

/**
 * Created by sanjay_dandekar on 02/10/14.
 */
public class EditionAdapter extends BaseAdapter {

    private ArrayList<Edition> editions;

    private LayoutInflater inflater = null;

    public EditionAdapter(Context context, ArrayList<Edition> editions) {
        this.editions = editions;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (editions != null) {
            return editions.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (editions != null) {
            return editions.get(i);
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
            view = inflater.inflate(R.layout.edition_row, null);
        }
        TextView tv = (TextView) view.findViewById(R.id.editionName);
        Edition ed = editions.get(i);
        tv.setText(ed.getEditionName());
        view.setBackgroundColor(adjustAlpha(ed.getColor(), 0.6f));
        return view;
    }

    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }
}
