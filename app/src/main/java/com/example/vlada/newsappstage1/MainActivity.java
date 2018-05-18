package com.example.vlada.newsappstage1;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsData>> {

    private static final String NEWS_REQUEST_URL = "https://content.guardianapis.com/search?show-tags=contributor&api-key=3969af52-ba0c-4003-a1a6-7c7705e4001a";

    // mEmptyStateNews is displayed when there is no data in the list
    private TextView mEmptyStateNews;

    // Adapter for news
    private NewsDataAdapter newsDataAdapter;

    // Constant value for the news loader ID
    private static final int NEWS_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the id listNews
        ListView listNews = findViewById(R.id.listNews);

        // Find the id empty_news
        mEmptyStateNews = findViewById(R.id.empty_news);
        listNews.setEmptyView(mEmptyStateNews);

        // Create new adapter that takes an empty list
        newsDataAdapter = new NewsDataAdapter(this, new ArrayList<NewsData>());
        listNews.setAdapter(newsDataAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected news
        listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                // Find the current news that was clicked on
                NewsData selectedNews = newsDataAdapter.getItem(position);
                // Convert the String URL into a URI object
                Uri selectedNewsUri = Uri.parse(selectedNews.getUrl());
                // Create a new intent to view the news URI
                Intent webIntent = new Intent(Intent.ACTION_VIEW, selectedNewsUri);
                // Send the intent to launch a new activity
                startActivity(webIntent);
            }
        });

        ConnectivityManager connectivityMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for the bundle.
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loader);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no internet connection error message
            mEmptyStateNews.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<NewsData>> onCreateLoader(int id, Bundle args) {

        // Create a new loader for the given URL
        return new DataLoader(this, NEWS_REQUEST_URL);

    }

    @Override
    public void onLoadFinished(Loader<List<NewsData>> loader, List<NewsData> data) {

        // Hide loading indicator because the data has been loaded
        View loadingProgress = findViewById(R.id.loader);
        loadingProgress.setVisibility(View.GONE);

        // Set empty state text to display
        mEmptyStateNews.setText(R.string.news_error);

        // Clear the adapter of previous data
        newsDataAdapter.clear();
        if (data != null && !data.isEmpty()) {
            newsDataAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsData>> loader) {
        newsDataAdapter.clear();
    }
}
