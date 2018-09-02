package com.edgeon.ecomapapp.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.edgeon.ecomapapp.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IssueDetails extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.issueArea)
    TextView issueArea;
    @BindView(R.id.issueType)
    TextView issueType;
    @BindView(R.id.issueDescription)
    TextView issueDescription;
    Typeface font, fontBold;
    @BindView(R.id.trackIssue)
    Button trackIssue;
    double lat;
    double lng;
    @BindView(R.id.issueImage)
    ImageView issueImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_details);
        ButterKnife.bind(this);

        font = Typeface.createFromAsset(getAssets(),
                "fonts/roboto.ttf");
        fontBold = Typeface.createFromAsset(getAssets(),
                "fonts/roboto_bold.ttf");

        issueArea.setTypeface(fontBold);
        issueType.setTypeface(fontBold);
        issueDescription.setTypeface(font);
        trackIssue.setTypeface(font);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Picasso.with(this).load(getIntent().getStringExtra("Image")).into(issueImage);
        issueType.setText(getIntent().getStringExtra("Type"));
        issueDescription.setText(getIntent().getStringExtra("Description"));
        issueArea.setText(getIntent().getStringExtra("Area"));

        lat = getIntent().getDoubleExtra("Lat", 1);
        lng = getIntent().getDoubleExtra("Long", 2);
    }

    @OnClick(R.id.trackIssue)
    public void onViewClicked() {

        Intent TrackIntent = new Intent(IssueDetails.this, Map.class);
        TrackIntent.putExtra("Lat", lat);
        TrackIntent.putExtra("Long", lng);
        startActivity(TrackIntent);
        finishAffinity();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
