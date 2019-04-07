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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
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

        loadMoviesData();
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void loadMoviesData() {
        showMovies();
        new FetchMoviesTask().execute();
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

    public class FetchMoviesTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mErrorMessageDisplay.setVisibility(View.INVISIBLE);
            mGridView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;

            try {
                // Create Apache HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                String url = urlString;
                HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    String response = EntityUtils.toString(httpResponse.getEntity());;
                    parseResult(response);
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed
                }
            } catch (Exception e) {
                Log.d("getData", "error");
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (result == 1) {
                mGridAdapter.setGridData(mGridData);
                showMovies();
            } else {
                showErrorMessage();
            }
        }

        String streamToString(InputStream stream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            String line;
            String moviesJsonStr = "";
            StringBuffer buffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            stream.close();

            return moviesJsonStr;
        }

        private void parseResult(String moviesJsonStr) {
            try {
                JSONObject movieBase = new JSONObject(moviesJsonStr);
                JSONArray moviesArray = movieBase.optJSONArray("results");
                Movie movie;

                for (int i = 0; i < moviesArray.length(); ++i) {
                    JSONObject currentMovie = moviesArray.optJSONObject(i);
                    movie = new Movie();
                    int id = currentMovie.optInt("id");
                    movie.setId(id);
                    String title = currentMovie.optString("title");
                    movie.setName(title);
                    String posterPath = currentMovie.optString("poster_path");
                    movie.setPosterPath(posterPath);
                    String overview = currentMovie.optString("overview");
                    movie.setOverview(overview);

                mGridData.add(movie);
            }
            } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
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

            loadMoviesData();
            mLoadingIndicator.setVisibility(View.VISIBLE);

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

            loadMoviesData();
            mLoadingIndicator.setVisibility(View.VISIBLE);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}



