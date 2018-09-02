package com.edgeon.ecomapapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edgeon.ecomapapp.R;
import com.edgeon.ecomapapp.utils.LocalDatabase;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import im.delight.android.location.SimpleLocation;

public class Map extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.relativeLayout2)
    RelativeLayout relativeLayout2;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.reportBtn)
    RelativeLayout reportBtn;
    @BindView(R.id.reportLayout)
    RelativeLayout reportLayout;
    @BindView(R.id.issue_layout)
    RelativeLayout issueLayout;
    @BindView(R.id.solution_layout)
    RelativeLayout solutionLayout;
    @BindView(R.id.goToSearch)
    RelativeLayout goToSearch;
    @BindView(R.id.search)
    TextView search;
    @BindView(R.id.reportTxt)
    TextView reportTxt;
    @BindView(R.id.issueTxt)
    TextView issueTxt;
    @BindView(R.id.solutionTxt)
    TextView solutionTxt;
    @BindView(R.id.closeBtn)
    RelativeLayout closeBtn;
    @BindView(R.id.pin)
    ImageView pin;
    private GoogleMap mMap;
    double latitude;
    double longitude;
    String toolbarTitle = "<font color=#A6D495>ECO</font><font color=#6B7177>MAP</font>";
    FirebaseAuth firebaseAuth;
    private SimpleLocation location;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    SupportMapFragment mapFragment;
    Typeface font, fontBold;
//    Marker marker;
    private static final int REQUEST_CHECK_SETTINGS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);


        font = Typeface.createFromAsset(getAssets(),
                "fonts/roboto.ttf");
        fontBold = Typeface.createFromAsset(getAssets(),
                "fonts/roboto_bold.ttf");

        reportTxt.setTypeface(fontBold);
        issueTxt.setTypeface(font);
        solutionTxt.setTypeface(font);

        location = new SimpleLocation(Map.this);

        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(Html.fromHtml(toolbarTitle));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_close, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();


        navView.setNavigationItemSelectedListener(this);


        Log.d("Lat",String.valueOf(getIntent().getDoubleExtra("Lat",location.getLatitude())));
        Log.d("Long",String.valueOf(getIntent().getDoubleExtra("Long",location.getLongitude())));

        latitude = getIntent().getDoubleExtra("Lat",location.getLatitude());
        longitude = getIntent().getDoubleExtra("Long",location.getLongitude());

//        if(getIntent().getDoubleExtra("Lat",1) == 1) {
//            latitude = location.getLatitude();
//            longitude = location.getLongitude();
//        }
//        else {
//            latitude = getIntent().getDoubleExtra("Lat",1);
//            longitude = getIntent().getDoubleExtra("Long",2);
//        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        displayLocationSettingsRequest(Map.this);

    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.e("LOG", "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.e("LOG", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(Map.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e("LOG", "PendingIntent unable to execute request");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e("LOG", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    private Bitmap getMarkerBitmapFromView() {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        //add marker to desired position
        LatLng loc = new LatLng(latitude, longitude);
//        marker = mMap.addMarker(new MarkerOptions().position(loc).title("Your Location").icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView())));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
//                if(marker != null)
//                    marker.remove();
                // Get the center of the Map.
                LatLng centerOfMap = mMap.getCameraPosition().target;
                latitude = centerOfMap.latitude;
                longitude = centerOfMap.longitude;
                // Update your Marker's position to the center of the Map.
//                marker.setPosition(centerOfMap);
//                marker = mMap.addMarker(new MarkerOptions().position(centerOfMap).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView())).
//                        anchor(0.5f, .05f).title("Your Location"));

            }
        });

    }


    @Override
    public void onBackPressed() {

        if (reportLayout.getVisibility() == View.VISIBLE) {
            // Its visible
            reportLayout.setVisibility(View.GONE);
        } else if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.statistics) {
            Log.d("Status", "Statistics");
            Intent in = new Intent(Map.this, Statistics.class);
            startActivity(in);
            this.finish();
        } else if (item.getItemId() == R.id.my_issues) {
            Log.d("Status", "Issues");
            Intent in = new Intent(Map.this, Home.class);
            startActivity(in);
            this.finish();
        } else if (item.getItemId() == R.id.my_solutions) {
            Log.d("Status", "Solutions");
            Intent in = new Intent(Map.this, Home.class);
            startActivity(in);
            this.finish();
        } else if (item.getItemId() == R.id.logout) {
            Log.d("Status", "Logout");
            AlertDialog.Builder builder = new AlertDialog.Builder(Map.this);
            builder.setTitle("Log Out");
            builder.setMessage("Are you sure you want to log out?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    firebaseAuth.signOut();
                    new LocalDatabase().clear(Map.this);
                    Intent i = new Intent(Map.this, Login.class);
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
        }
        return true;
    }


    @OnClick({R.id.issue_layout, R.id.solution_layout, R.id.reportBtn, R.id.search, R.id.pin, R.id.closeBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.issue_layout:
                Intent in = new Intent(Map.this, Issue.class);
                in.putExtra("lat", String.valueOf(latitude));
                in.putExtra("lng", String.valueOf(longitude));
                startActivity(in);
                break;
            case R.id.solution_layout:
                Intent sol = new Intent(Map.this, Solution.class);
                sol.putExtra("lat", String.valueOf(latitude));
                sol.putExtra("lng", String.valueOf(longitude));
                startActivity(sol);
                break;
            case R.id.reportBtn:
                reportLayout.setVisibility(View.VISIBLE);
                //Crashlytics.getInstance().crash();
                break;
            case R.id.closeBtn:
                drawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.pin:
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                LatLng loc = new LatLng(latitude, longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
//                marker.setPosition(loc);
                break;
            case R.id.search:
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                mMap.clear();
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                LatLng loc = new LatLng(latitude, longitude);
                mMap.setMyLocationEnabled(true);
//                marker = mMap.addMarker(new MarkerOptions().position(loc).title(place.getName().toString()).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView())));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                    @Override
                    public void onCameraMove() {
                        // Get the center of the Map.
                        LatLng centerOfMap = mMap.getCameraPosition().target;
//                        if(marker != null)
//                           marker.remove();
                        latitude = centerOfMap.latitude;
                        longitude = centerOfMap.longitude;
                        // Update your Marker's position to the center of the Map.
//                        marker.setPosition(centerOfMap);
//                        marker = mMap.addMarker(new MarkerOptions().position(centerOfMap).anchor(0.5f, .05f).title("Test"));
                    }
                });

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("LOG", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


}
