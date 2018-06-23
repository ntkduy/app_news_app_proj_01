package com.ntkduy1604.newsapp.fragment;


import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ntkduy1604.newsapp.R;
import com.ntkduy1604.newsapp.constants.Constants;
import com.ntkduy1604.newsapp.json.JsonLoader;
import com.ntkduy1604.newsapp.model.Article;
import com.ntkduy1604.newsapp.recycling.ArticleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SportFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<Article>>
        , SharedPreferences.OnSharedPreferenceChangeListener
    {
    private static final String TAG = "SportFragment";
    @BindString(R.string.no_internet_connection) String NO_CONNECTION;
    @BindString(R.string.nav_sport) String sectionName;
    @BindString(R.string.settings_order_by_key) String settingsOrderByKey;
    @BindString(R.string.settings_order_by_default) String settingsOrderByValueDefault;
    @BindString(R.string.settings_edition_key) String settingsEditionKey;
    @BindString(R.string.settings_edition_default) String settingsEditionValueDefault;

    @BindView(R.id.tv_section_name) TextView tvSectionName;
    @BindView(R.id.empty_view) TextView tvEmptyStateTextView;
    @BindView(R.id.recyclerView) RecyclerView rvRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar pbvProgressBarView;

    public SportFragment() {
        // Required empty public constructor
    }

    protected ArticleAdapter articleAdapter;
    protected RecyclerView.LayoutManager layoutManager;
    protected ArrayList<Article> artilesList = new ArrayList<>();

    private SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view_frag, container, false);
        rootView.setTag(TAG);
        ButterKnife.bind(this, rootView);

        tvSectionName.setText(sectionName);

        layoutManager = new LinearLayoutManager(getActivity());
        rvRecyclerView.setLayoutManager(layoutManager);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(this);

        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected) {
            LoaderManager loaderManager = getActivity().getLoaderManager();
            loaderManager.initLoader(Constants.SPORT_LOADER_ID, null, this);
        } else {
            pbvProgressBarView.setVisibility(View.GONE);
            tvEmptyStateTextView.setVisibility(View.VISIBLE);
            tvEmptyStateTextView.setText(NO_CONNECTION);
        }
        return rootView;
    }
        @Override
        public void onDestroy(){
            super.onDestroy();
            prefs.unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause(){
            super.onPause();
            prefs.unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onResume(){
            super.onResume();
            prefs.registerOnSharedPreferenceChangeListener(this);
        }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals(settingsEditionKey) | key.equals(settingsOrderByKey)){
            // Clear the ListView as a new query will be kicked off
            //artilesList.clear();

            // Hide the empty state text view as the loading indicator will be displayed
            tvEmptyStateTextView.setVisibility(View.GONE);

            // Show the loading indicator while new data is being fetched
            pbvProgressBarView.setVisibility(View.VISIBLE);

            // Restart the loader to requery the USGS as the query settings have been updated
            LoaderManager loaderManager = getActivity().getLoaderManager();
            loaderManager.restartLoader(Constants.SPORT_LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String orderBy = sharedPrefs.getString(settingsOrderByKey, settingsOrderByValueDefault);
        String edition = sharedPrefs.getString(settingsEditionKey, settingsEditionValueDefault);


        Uri baseUri = Uri.parse(Constants.REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter(Constants.API_SECTION, Constants.SPORT_SECTION);
        uriBuilder.appendQueryParameter(Constants.API_FIELD, Constants.AUTHOR_FIELD);
        uriBuilder.appendQueryParameter(Constants.API_KEY, Constants.PERSONAL_API_KEY);
        uriBuilder.appendQueryParameter(Constants.ORDER_BY, orderBy);
        uriBuilder.appendQueryParameter(Constants.EDITION, edition);

        return new JsonLoader(getActivity(), uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        pbvProgressBarView.setVisibility(View.GONE);
        tvEmptyStateTextView.setVisibility(View.VISIBLE);
        tvEmptyStateTextView.setText(R.string.no_info);

        if (articles != null && !articles.isEmpty()) {
            tvEmptyStateTextView.setVisibility(View.GONE);
            artilesList = (ArrayList<Article>) articles;
            articleAdapter = new ArticleAdapter(artilesList);
            rvRecyclerView.setAdapter(articleAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        // Loader reset, so we can clear out our existing data.
        artilesList.clear();
    }
}
