package com.dandekar.epaper;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.dandekar.epaper.adapter.EditionAdapter;
import com.dandekar.epaper.model.Edition;

import java.util.ArrayList;


public class MainActivity extends ListActivity {

    private static ArrayList<Edition> editions = new ArrayList<Edition>();

    private EditionAdapter adapter = null;

    static {
        Edition ed = new Edition("Ahmedabad","31805", Color.BLUE);
        editions.add(ed);
        ed = new Edition("Bangalore","31806", Color.CYAN);
        editions.add(ed);
        ed = new Edition("Chennai","31807", Color.LTGRAY);
        editions.add(ed);
        ed = new Edition("Delhi","31808", Color.GREEN);
        editions.add(ed);
        ed = new Edition("Hyderabad","31809", Color.MAGENTA);
        editions.add(ed);
        ed = new Edition("Jaipur","31810", Color.RED);
        editions.add(ed);
        ed = new Edition("Kochi","31811", Color.YELLOW);
        editions.add(ed);
        ed = new Edition("Kolkata","31812", Color.BLUE);
        editions.add(ed);
        ed = new Edition("Lucknow","31813", Color.CYAN);
        editions.add(ed);
        ed = new Edition("Mumbai","31804", Color.LTGRAY);
        editions.add(ed);
        ed = new Edition("NaviMumbai","31840", Color.GREEN);
        editions.add(ed);
        ed = new Edition("Pune","31814", Color.MAGENTA);
        editions.add(ed);
        ed = new Edition("Thane","31839", Color.RED);
        editions.add(ed);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.dandekar.epaper.R.layout.activity_main);
        adapter = new EditionAdapter(this, editions);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Edition ed = editions.get(position);
        Intent i = new Intent(MainActivity.this, PageListActivity.class);
        i.putExtra("EDITION", ed);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.dandekar.epaper.R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == com.dandekar.epaper.R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
