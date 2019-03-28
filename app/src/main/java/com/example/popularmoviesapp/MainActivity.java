package com.example.popularmoviesapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.popularmoviesapp.BuildConfig.API_KEY;

public class MainActivity extends AppCompatActivity {

    String urlString;
    private ProgressBar mLoadingIndicator;
    private GridView mGridView;
    private GridViewAdapter mGridAdapter;
    private TextView mErrorMessageDisplay;
    private ArrayList<Movie> mGridData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        mGridView = (GridView) findViewById(R.id.gridView);
        mErrorMessageDisplay = (TextView) findViewById(R.id.error_message_display);

        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);

        new FetchMoviesTask().execute();
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.popularSort) {

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath("top_rated")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("language", "en-US");
            urlString = builder.build().toString();

            return true;
        }

        if (id == R.id.ratingSort) {

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath("popular")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("language", "en-US")
                    .appendQueryParameter("page", "1");
            urlString = builder.build().toString();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showErrorMessage() {
        mGridView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    private void showMovies() {
        mGridView.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mErrorMessageDisplay.setVisibility(View.INVISIBLE);
            mGridView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesJsonStr = null;

            try {

                if (urlString == null){
                    Uri.Builder builder = new Uri.Builder();
                    builder.scheme("https")
                            .authority("api.themoviedb.org")
                            .appendPath("3")
                            .appendPath("movie")
                            .appendPath("popular")
                            .appendQueryParameter("api_key", API_KEY)
                            .appendQueryParameter("language", "en-US")
                            .appendQueryParameter("page", "1");
                    urlString = builder.build().toString();
                }

                URL url = new URL(urlString);
                // Create the request and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                moviesJsonStr = buffer.toString();

                try {

                JSONObject movieBase = new JSONObject(moviesJsonStr);

                JSONArray moviesArray = movieBase.getJSONArray("results");
                Movie movies[] = new Movie[moviesArray.length()];

                for (int i = 0; i < moviesArray.length(); ++i) {
                    JSONObject currentMovie = moviesArray.getJSONObject(i);
                    movies[i] = new Movie((currentMovie.getInt("id")),
                            (currentMovie.getString("title")),
                            (currentMovie.getString("poster_path")),
                            (currentMovie.getString("overview")));
                }
                return movies;

            } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }

            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // The code didn't successfully get the weather data
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movies != null) {
                ArrayList<Movie> moviesList = new ArrayList<Movie>(movies.length);
                mGridAdapter.setGridData(moviesList);
                showMovies();
            } else {
                showErrorMessage();
            }
        }
    }
}



