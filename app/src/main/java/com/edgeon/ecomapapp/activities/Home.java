package com.edgeon.ecomapapp.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edgeon.ecomapapp.R;
import com.edgeon.ecomapapp.adapters.ViewPagerAdapter;
import com.edgeon.ecomapapp.utils.LocalDatabase;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    ViewPagerAdapter viewPagerAdapter;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    FirebaseAuth firebaseAuth;
    Typeface font;
    String toolbarTitle = "<font color=#A6D495>ECO</font><font color=#6B7177>MAP</font>";
    @BindView(R.id.closeBtn)
    RelativeLayout closeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        font = Typeface.createFromAsset(getAssets(),
                "fonts/roboto_bold.ttf");

        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(Html.fromHtml(toolbarTitle));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        @SuppressLint("ResourceType") ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_close, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewpager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        changeTabsFont();
        //setupVerticalDivider();

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();


    }

    private void changeTabsFont() {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(font);
                }
            }
        }
    }

    private void setupVerticalDivider() {
        View root = tabLayout.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(Color.BLACK);
            drawable.setSize(1, 1);
            ((LinearLayout) root).setDividerPadding(5);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Log.d("Clicked", "Clicked");
        item.setChecked(true);
        if (item.getItemId() == R.id.statistics) {
            Log.d("Status", "Statistics");
            Intent in = new Intent(Home.this, Statistics.class);
            startActivity(in);
            this.finish();
        } else if (item.getItemId() == R.id.my_issues) {
            Log.d("Status", "Issues");
            Intent in = new Intent(Home.this, Home.class);
            startActivity(in);
            this.finish();
        } else if (item.getItemId() == R.id.my_solutions) {
            Log.d("Status", "Solutions");
            Intent in = new Intent(Home.this, Home.class);
            startActivity(in);
            this.finish();
        } else if (item.getItemId() == R.id.logout) {
            Log.d("Status", "Logout");
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            builder.setTitle("Log Out");
            builder.setMessage("Are you sure you want to log out?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    firebaseAuth.signOut();
                    new LocalDatabase().clear(Home.this);
                    Intent i = new Intent(Home.this, Login.class);
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
            Intent in = new Intent(Home.this, Map.class);
            startActivity(in);
            this.finish();
        }

        return true;
    }

    @OnClick(R.id.closeBtn)
    public void onViewClicked() {
        drawerLayout.closeDrawer(Gravity.LEFT);
    }
}
