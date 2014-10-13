package com.dandekar.epaper.utility;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by sanjay_dandekar on 02/10/14.
 */
public class Utility {

    public static byte[] readFile(File file) {
        // Create buffer big enough to hold the file content
        byte[] fileBuffer = new byte[(int)file.length()];
        // Create file input stream
        FileInputStream stream;
        try {
            stream = new FileInputStream(file);
            int offset = 0x0;
            int bytesRead = 0x0;
            while (offset < fileBuffer.length
                    && (bytesRead = stream.read(fileBuffer, offset,
                    fileBuffer.length - offset)) >= 0) {
                offset += bytesRead;
            }
            stream.close();
            stream = null;
        } catch (Exception ex) {
            ex.printStackTrace();
            fileBuffer = null;
        }
        return fileBuffer;
    }

    public static void write(File file, byte[] fileBuffer) {
        // Create file output stream
        FileOutputStream stream;
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
            stream = new FileOutputStream(file, false);
            stream.write(fileBuffer, 0, fileBuffer.length);
            stream.flush();
            stream.close();
            stream = null;
        } catch (Exception ex) {
            ex.printStackTrace();
            fileBuffer = null;
        }
    }

    public static byte[] performHttpGet(String url) {
        byte[] fileData = null;
        try {
            // Get the data from web
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(url);
            HttpResponse response = client.execute(get);
            fileData = EntityUtils.toByteArray(response.getEntity());
        } catch (Exception ex) {
            ex.printStackTrace();
            fileData = null;
        }
        return fileData;
    }
}
