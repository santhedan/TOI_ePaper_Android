package com.dandekar.epaper.tasks;

import android.os.AsyncTask;
import android.os.Environment;

import com.dandekar.epaper.model.GetDayIndexTaskInput;
import com.dandekar.epaper.model.GetDayIndexTaskOutputEvent;
import com.dandekar.epaper.model.Page;
import com.dandekar.epaper.parser.PageParser;
import com.dandekar.epaper.utility.MyBus;
import com.dandekar.epaper.utility.Utility;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Created by sanjay_dandekar on 02/10/14.
 */
public class GetDayIndexTask extends AsyncTask<GetDayIndexTaskInput, Void, List<Page>> {

    private static String DAYINDEX_FILE = "/DayIndex.xml";

    private static String URL = "http://epaperbeta.timesofindia.com/index.aspx?eid=%s&dt=%s";

    private static final String NEEDLE = "var strDayIndex = '";
    private static final String END_NEEDLE = "'";

    @Override
    protected List<Page> doInBackground(GetDayIndexTaskInput... getDayIndexTaskInputs) {
        // Get the input data
        GetDayIndexTaskInput input = getDayIndexTaskInputs[0];
        // Check if the day index is cached or not?
        File pathToDayData = new File(String.format("%s/%s/%s", Environment.getExternalStorageDirectory(), input.getDate(), input.getEdition()));
        // Day index file
        File dayIndexFile = new File(String.format("%s/%s", pathToDayData.getAbsolutePath(), DAYINDEX_FILE));
        //
        byte[] fileData = null;
        // does the directory exist?
        if (dayIndexFile.exists()) {
            // Read the file
            fileData = Utility.readFile(dayIndexFile);
        } else {
            fileData = Utility.performHttpGet(String.format(URL, input.getEdition(), input.getDate()));
            Utility.write(dayIndexFile, fileData);
        }
        //
        if (fileData != null) {
            // Convert byte to string
            String respStr = new String(fileData);
            // Find start of XML
            int startIndex = respStr.indexOf(NEEDLE);
            if (startIndex > 0) {
                // modify start index
                startIndex = startIndex + NEEDLE.length();
                // Now fild end needle
                int endIndex = respStr.indexOf(END_NEEDLE, startIndex);
                if (endIndex > 0) {
                    // Get XML data
                    String xmlData = respStr.substring(startIndex, endIndex);
                    // Create inout stream
                    InputStream in = new ByteArrayInputStream(xmlData.getBytes());
                    // Parse the data
                    PageParser.cityName = input.getCityName();
                    List<Page> pages = PageParser.parse(in, pathToDayData.getAbsolutePath());
                    //
                    return pages;
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Page> pages) {
        if (!isCancelled()) {
            if (pages != null) {
                /*
                for (Page p : pages) {
                    Log.e("TOI", p.toString());
                    if (p.getArticles() != null) {
                        for (Article a : p.getArticles()) {
                            Log.e("TOI", a.toString());
                        }
                    }
                }*/
                MyBus.getInstance().post(new GetDayIndexTaskOutputEvent(pages));
            } else {
                MyBus.getInstance().post(new GetDayIndexTaskOutputEvent(null));
            }
        }
    }
}
