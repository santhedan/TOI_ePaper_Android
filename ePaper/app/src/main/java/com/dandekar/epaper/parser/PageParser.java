package com.dandekar.epaper.parser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Xml;

import com.dandekar.epaper.PageListActivity;
import com.dandekar.epaper.model.Article;
import com.dandekar.epaper.model.Page;
import com.dandekar.epaper.utility.Utility;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanjay_dandekar on 02/10/14.
 */
public class PageParser {

    // We don't use namespaces
    private static final String ns = null;

    private static final String IMAGE_URL = "http://epaperbeta.timesofindia.com/NasData//PUBLICATIONS/THETIMESOFINDIA/%s/%s/%s/%s/Article/%s/%s_%s_%s_%s_%s.jpg";

    private static final String PAGE_THUMB_URL = "http://epaperbeta.timesofindia.com/NasData//PUBLICATIONS/THETIMESOFINDIA/%s/%s/%s/%s/Page/%s.jpg";

    public static String cityName = "";

    private static volatile Matrix sScaleMatrix;

    private static final Paint SCALE_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG |
            Paint.FILTER_BITMAP_FLAG);

    private static final Paint SHADOW_PAINT = new Paint();

    public static List<Page> parse (InputStream in, String pathToSaveBitmap) {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readPages(parser, pathToSaveBitmap);
        } catch (XmlPullParserException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static List<Page> readPages(XmlPullParser parser, String pathToSaveBitmap) throws IOException, XmlPullParserException {
        List<Page> pages = new ArrayList<Page>();
        parser.require(XmlPullParser.START_TAG, ns, "DayIndex");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Page")) {
                Page p = readPage(parser, pathToSaveBitmap);
                if (p != null) {
                    pages.add(p);
                }
            } else {
                skip(parser);
            }
        }
        return pages;
    }

    private static Page readPage(XmlPullParser parser, String pathToSaveBitmap) throws IOException, XmlPullParserException {

        Page page = null;

        parser.require(XmlPullParser.START_TAG, ns, "Page");

        String title = parser.getAttributeValue(null, "title");
        String name = parser.getAttributeValue(null, "name");
        List<Article> articles = new ArrayList<Article>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String attrName = parser.getName();
            if (attrName.equals("Article")) {
                Article article = readArticle(parser);
                if (article != null) {
                    articles.add(article);
                }
            } else {
                skip(parser);
            }
        }
        //
        if (articles != null && articles.size() > 0) {
            //
            Bitmap resizedBM = null;
            String pageThumbnailUrl = "";
            // Do we have the thumbnail stored or do we have to download it?
            String thumbnailStorePath = pathToSaveBitmap + "/" + name;
            File thumbFile = new File(thumbnailStorePath);
            if (thumbFile.exists()) {
                byte[] thumbnailData = Utility.readFile(thumbFile);
                resizedBM = BitmapFactory.decodeByteArray(thumbnailData, 0, thumbnailData.length);
            } else {
                // Get name of the first article
                String articleName = articles.get(0).getName();
                //
                String imageParts = articleName.substring(articleName.length() - 14);
                String day = imageParts.substring(0, 2);
                String month = imageParts.substring(2, 4);
                String year = imageParts.substring(4, 8);
                //
                pageThumbnailUrl = String.format(PAGE_THUMB_URL, cityName, year, month, day, name);
                //
                byte[] thumbnailData = Utility.performHttpGet(pageThumbnailUrl);
                Bitmap bitmap = BitmapFactory.decodeByteArray(thumbnailData, 0, thumbnailData.length);
                resizedBM = createScaledBitmap(bitmap, PageListActivity.width, PageListActivity.height, 0, false, SHADOW_PAINT);
                //
                bitmap.recycle();
                // Save the resized thumbnail on disc
                ByteArrayOutputStream baos= new ByteArrayOutputStream();
                resizedBM.compress(Bitmap.CompressFormat.PNG, 100, baos);
                thumbnailData = baos.toByteArray();
                Utility.write(thumbFile, thumbnailData);
            }
            //
            page = new Page(name, title, pageThumbnailUrl, articles, resizedBM);
        }
        //
        parser.require(XmlPullParser.END_TAG, ns, "Page");
        return page;
    }

    private static Article readArticle(XmlPullParser parser) throws IOException, XmlPullParserException {

        Article article = null;
        String articleTitle = null;
        String articleBody = null;

        parser.require(XmlPullParser.START_TAG, ns, "Article");

        String name = parser.getAttributeValue(null, "name");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String attrName = parser.getName();
            if (attrName.equals("ArticleTitle")) {
                articleTitle = readArticleTitle(parser);
            } else if (attrName.equals("ArticleBody")) {
                articleBody = readArticleBody(parser);
            } else {
                skip(parser);
            }
        }

        if (!((articleTitle.toUpperCase().contains("TIMES") || articleTitle.toUpperCase().contains("DATE LINE")) && articleBody.contains("..."))) {

            String imageParts = name.substring(name.length() - 14);

            String day = imageParts.substring(0, 2);
            String month = imageParts.substring(2, 4);
            String year = imageParts.substring(4, 8);
            String pageNumber = imageParts.substring(8, 11);
            String articleNumber = imageParts.substring(11, 14);

            String articleImageUrl = String.format(IMAGE_URL, cityName, year, month, day, pageNumber, day, month, year, pageNumber, articleNumber);

            article = new Article(name, articleTitle, articleBody, articleImageUrl);
        }
        parser.require(XmlPullParser.END_TAG, ns, "Article");

        return article;
    }

    private static String readArticleTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "ArticleTitle");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "ArticleTitle");
        return title;
    }

    private static String readArticleBody(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "ArticleBody");
        String body = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "ArticleBody");
        return body;
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private static Bitmap createScaledBitmap(Bitmap src, int dstWidth, int dstHeight,
                                             float offset, boolean clipShadow, Paint paint) {

        Matrix m;
        synchronized (Bitmap.class) {
            m = sScaleMatrix;
            sScaleMatrix = null;
        }

        if (m == null) {
            m = new Matrix();
        }

        final int width = src.getWidth();
        final int height = src.getHeight();
        final float sx = dstWidth  / (float) width;
        final float sy = dstHeight / (float) height;
        m.setScale(sx, sy);

        Bitmap b = createBitmap(src, 0, 0, width, height, m, offset, clipShadow, paint);

        synchronized (Bitmap.class) {
            sScaleMatrix = m;
        }

        return b;
    }

    private static Bitmap createBitmap(Bitmap source, int x, int y, int width,
                                       int height, Matrix m, float offset, boolean clipShadow, Paint paint) {

        int scaledWidth = width;
        int scaledHeight = height;

        final Canvas canvas = new Canvas();
        canvas.translate(offset / 2.0f, offset / 2.0f);

        Bitmap bitmap;

        final Rect from = new Rect(x, y, x + width, y + height);
        final RectF to = new RectF(0, 0, width, height);

        if (m == null || m.isIdentity()) {
            bitmap = Bitmap.createBitmap(scaledWidth + (int) offset,
                    scaledHeight + (int) (clipShadow ? (offset / 2.0f) : offset),
                    Bitmap.Config.ARGB_8888);
            paint = null;
        } else {
            RectF mapped = new RectF();
            m.mapRect(mapped, to);

            scaledWidth = Math.round(mapped.width());
            scaledHeight = Math.round(mapped.height());

            bitmap = Bitmap.createBitmap(scaledWidth + (int) offset,
                    scaledHeight + (int) (clipShadow ? (offset / 2.0f) : offset),
                    Bitmap.Config.ARGB_8888);
            canvas.translate(-mapped.left, -mapped.top);
            canvas.concat(m);
        }

        canvas.setBitmap(bitmap);
        canvas.drawRect(0.0f, 0.0f, width, height, paint);
        canvas.drawBitmap(source, from, to, SCALE_PAINT);

        return bitmap;
    }

}
