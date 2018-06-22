package com.ntkduy1604.newsapp.json;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.ntkduy1604.newsapp.model.Article;

import java.util.List;

/**
 * Created by NTKDUY on 6/14/2018
 * for PIGGY HOUSE
 * you can contact me at: ntkduy1604@gmail.com
 */
public class JsonLoader extends AsyncTaskLoader<List<Article>> {

    /** Tag for log messages */
    private static final String LOG_TAG = JsonLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link JsonLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public JsonLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Article> loadInBackground() {
        if (null == mUrl) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of dataModels.
        return QueryUtils.fetchEarthquakeData(mUrl);
    }
}
