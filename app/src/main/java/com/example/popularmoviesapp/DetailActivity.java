package com.example.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class DetailActivity extends AppCompatActivity {

    private ImageView mImage;
    private TextView mName;
    private TextView mOverview;
    private TextView mVoteAverage;
    private TextView mReleaseDate;
    String base_url = "http://image.tmdb.org/t/p/w185";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        mImage = (ImageView) findViewById(R.id.image_iv);
        mName = (TextView) findViewById(R.id.nameIv);
        mOverview   = (TextView) findViewById(R.id.overviewIv);
        mVoteAverage = (TextView) findViewById(R.id.voteAverage);
        mReleaseDate = (TextView) findViewById(R.id.releaseDate);

        Intent intent = getIntent();

        if(intent.hasExtra("name")){
            String title = intent.getStringExtra("name");
            mName.setText(title);
        }

        if(intent.hasExtra("overview")){
            String overview = intent.getStringExtra("overview");
            mOverview.setText(overview);
        }

        if(intent.hasExtra("posterPath")){
            String posterPath = intent.getStringExtra("posterPath");
            Picasso.get().load(base_url + posterPath).into(mImage);

        }

        if(intent.hasExtra("voteAverage")){
            String voteAverage = "Vote Average: " + intent.getStringExtra("voteAverage");
            mVoteAverage.setText(voteAverage);
        }

        if(intent.hasExtra("releaseDate")){
            String releaseDate = "Release Date: " + intent.getStringExtra("releaseDate");
            mReleaseDate.setText(releaseDate);
        }
    }
}
