package com.dandekar.epaper;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dandekar.epaper.adapter.PageAdapter;
import com.dandekar.epaper.model.Article;
import com.dandekar.epaper.model.Edition;
import com.dandekar.epaper.model.GetDayIndexTaskInput;
import com.dandekar.epaper.model.GetDayIndexTaskOutputEvent;
import com.dandekar.epaper.model.Page;
import com.dandekar.epaper.tasks.GetDayIndexTask;
import com.dandekar.epaper.utility.MyBus;
import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PageListActivity extends Activity implements AdapterView.OnItemClickListener {

    private Edition edition = null;

    private Date date = new Date();

    private Date today = null;

    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    private SimpleDateFormat displayFormat = new SimpleDateFormat("dd-MMM-yyyy");

    private GridView gv = null;

    private GetDayIndexTask task = null;

    private AtomicBoolean fetchInProgress = new AtomicBoolean(false);

    public static int width = 0;
    public static int height = 0;

    private LinearLayout progressLayout = null;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayShowHomeEnabled(false);
        setContentView(R.layout.activity_page_list);
        //
        progressLayout = (LinearLayout) findViewById(R.id.progressLayout);
        //
        edition = (Edition) getIntent().getSerializableExtra("EDITION");
        //
        today = date;
        String title = String.format("ToI %s(%s)", edition.getEditionName(), displayFormat.format(date));
        this.setTitle(title);
        //
        String dateStr = format.format(date);
        // Now start the async task
        GetDayIndexTaskInput input = new GetDayIndexTaskInput(edition.getEditionId(), edition.getEditionName(), dateStr);
        // configure progress bar
        //
        task = new GetDayIndexTask();
        task.execute(input);
        fetchInProgress.set(true);
        progressLayout.setVisibility(View.VISIBLE);
        //
        MyBus.getInstance().register(this);
        //
        gv = (GridView) findViewById(R.id.gridview);
        gv.setOnItemClickListener(this);
        //
        Resources r = getResources();
        width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 220, r.getDisplayMetrics());
        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 338, r.getDisplayMetrics());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.page_list, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!fetchInProgress.get()) {
            // get today's date in string format
            String todayStr = format.format(today);
            // Get date's date in string format
            String dateStr = format.format(date);
            // if they are equal then next has to be disabled / not shown
            if (todayStr.equalsIgnoreCase(dateStr)) {
                menu.findItem(R.id.action_next).setEnabled(false);
            } else {
                menu.findItem(R.id.action_next).setEnabled(true);
            }
            menu.findItem(R.id.action_previous).setEnabled(true);
            return super.onPrepareOptionsMenu(menu);
        } else {
            menu.findItem(R.id.action_next).setEnabled(false);
            menu.findItem(R.id.action_previous).setEnabled(false);
            return super.onPrepareOptionsMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_next) {
            if (task != null) {
                task.cancel(true);
            }
            navigate(1);
            return true;
        } else if (id == R.id.action_previous) {
            if (task != null) {
                task.cancel(true);
            }
            navigate(-1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void navigate(int day) {
        if (!fetchInProgress.get()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, day);
            date = cal.getTime();
            String dateStr = format.format(date);
            // Now start the async task
            GetDayIndexTaskInput input = new GetDayIndexTaskInput(edition.getEditionId(), edition.getEditionName(), dateStr);
            //
            task = new GetDayIndexTask();
            task.execute(input);
            fetchInProgress.set(true);
            progressLayout.setVisibility(View.VISIBLE);
            //
            String title = String.format("ToI %s(%s)", edition.getEditionName(), displayFormat.format(date));
            this.setTitle(title);
        } else {
            Toast.makeText(this, "Please wait", Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onDestroy() {
        MyBus.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Subscribe
    public void onGetDayIndexTaskOutput (GetDayIndexTaskOutputEvent event) {
        fetchInProgress.set(false);
        progressLayout.setVisibility(View.GONE);
        Log.e("TOI", "pages.size() -> " + event.getPages().size());
        task = null;
        if (event.getPages() != null) {
            gv.setAdapter(new PageAdapter(event.getPages(), this));
        } else {
            Toast.makeText(this, "Unable to get ePaper. ePaper is only accessible in India", Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // Get article list
        Page page = ((Page)gv.getAdapter().getItem(i));
        // Create intent
        Intent intent = new Intent(this, ArticleListActivity.class);
        intent.putExtra("PAGE", page);
        startActivity(intent);
    }
}
