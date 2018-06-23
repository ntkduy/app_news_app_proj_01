package com.ntkduy1604.newsapp.fragment;


import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
public class SportFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Article>>{
    private static final String TAG = "NewsFragment";
    @BindString(R.string.no_internet_connection) String NO_CONNECTION;
    @BindString(R.string.nav_sport) String sectionName;

    @BindView(R.id.tv_section_name) TextView tvSectionName;
    @BindView(R.id.empty_view) TextView tvEmptyStateTextView;
    @BindView(R.id.recyclerView) RecyclerView rvRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar pbvProgressBarView;

    private static final String REQUEST_URL =
            "https://content.guardianapis.com/search?"
                    + "&section=" + Constants.SPORT_SECTION
                    + "&show-fields=" + Constants.AUTHOR_FIELD
                    + "&api-key=" + Constants.GUARDIAN_API_KEY;

    public SportFragment() {
        // Required empty public constructor
    }

    protected ArticleAdapter articleAdapter;
    protected RecyclerView.LayoutManager layoutManager;
    protected ArrayList<Article> artilesList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view_frag, container, false);
        rootView.setTag(TAG);
        ButterKnife.bind(this, rootView);

        tvSectionName.setText(sectionName);
        tvEmptyStateTextView.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(getActivity());
        rvRecyclerView.setLayoutManager(layoutManager);

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
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        return new JsonLoader(getActivity(), REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        pbvProgressBarView.setVisibility(View.GONE);
        tvEmptyStateTextView.setText(R.string.no_info);

        if (articles != null && !articles.isEmpty()) {
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
