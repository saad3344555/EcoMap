package com.edgeon.ecomapapp.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edgeon.ecomapapp.R;
import com.edgeon.ecomapapp.fragments.BarGraph;
import com.edgeon.ecomapapp.fragments.PieChart;
import com.edgeon.ecomapapp.utils.LocalDatabase;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Statistics extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    FirebaseAuth firebaseAuth;
    String toolbarTitle = "<font color=#A6D495>ECO</font><font color=#6B7177>MAP</font>";
    @BindView(R.id.typeWise)
    TextView typeWise;
    @BindView(R.id.areaWise)
    TextView areaWise;
    boolean type = true, area = false;
    @BindView(R.id.statisticsTxt)
    TextView statisticsTxt;
    Typeface font, fontBold;
    @BindView(R.id.closeBtn)
    RelativeLayout closeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        ButterKnife.bind(this);

        font = Typeface.createFromAsset(getAssets(),
                "fonts/roboto.ttf");
        fontBold = Typeface.createFromAsset(getAssets(),
                "fonts/roboto_bold.ttf");

        statisticsTxt.setTypeface(font);

        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(Html.fromHtml(toolbarTitle));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navView.setNavigationItemSelectedListener(this);

        @SuppressLint("ResourceType") ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_close, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment, new PieChart())
                .commit();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.my_issues) {
            Log.d("Status", "Issues");
            Intent in = new Intent(Statistics.this, Home.class);
            startActivity(in);
            this.finish();
        } else if (item.getItemId() == R.id.my_solutions) {
            Log.d("Status", "Solutions");
            Intent in = new Intent(Statistics.this, Home.class);
            startActivity(in);
            this.finish();
        } else if (item.getItemId() == R.id.logout) {
            Log.d("Status", "Logout");
            AlertDialog.Builder builder = new AlertDialog.Builder(Statistics.this);
            builder.setTitle("Log Out");
            builder.setMessage("Are you sure you want to log out?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    firebaseAuth.signOut();
                    new LocalDatabase().clear(Statistics.this);
                    Intent i = new Intent(Statistics.this, Login.class);
                    startActivity(i);
                    finishAffinity();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } else if (item.getItemId() == R.id.report) {
            Log.d("Status", "Report Issue");
            Intent in = new Intent(Statistics.this, Map.class);
            startActivity(in);
            this.finish();
        }
        return true;
    }

    @OnClick({R.id.typeWise, R.id.areaWise})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.typeWise:
                if (!type) {
                    areaWise.setTextColor(Color.parseColor("#37414B"));
                    areaWise.setBackground(getResources().getDrawable(R.drawable.stats_filter_bg));

                    typeWise.setTextColor(getResources().getColor(R.color.colorPrimary));
                    typeWise.setBackground(getResources().getDrawable(R.drawable.stats_filter_bg_filled));

                    type = true;
                    area = false;
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, new PieChart())
                            .commit();
                }
                break;
            case R.id.areaWise:
                if (!area) {
                    typeWise.setTextColor(Color.parseColor("#37414B"));
                    typeWise.setBackground(getResources().getDrawable(R.drawable.stats_filter_bg));

                    areaWise.setTextColor(getResources().getColor(R.color.colorPrimary));
                    areaWise.setBackground(getResources().getDrawable(R.drawable.stats_filter_bg_filled));

                    type = false;
                    area = true;
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment, new BarGraph())
                            .commit();
                }
                break;
        }
    }

    @OnClick(R.id.closeBtn)
    public void onViewClicked() {
        drawerLayout.closeDrawer(Gravity.LEFT);
    }
}