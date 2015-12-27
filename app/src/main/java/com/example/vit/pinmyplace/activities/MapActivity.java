package com.example.vit.pinmyplace.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.vit.pinmyplace.R;
import com.example.vit.pinmyplace.models.UserLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.parceler.Parcels;

import java.util.List;

/**
 * Get the list of UserLocation {@link UserLocation} and show them on the map
 * as markers
 *
 * @author Vit
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    List<UserLocation> locations;
    MapFragment mapFragment;

    GoogleMap googleMap;
    GoogleApiClient googleApiClient;

    public static final float MAP_ZOOM = 8.0f;
    public static final int MAP_MOVE_CAMERA_DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.locations = Parcels.unwrap(extras.getParcelable("locations"));
        }

        initToolbar();
        initGoogleApiClient();
        setupMapFragment();
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    private void initToolbar(){
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupMapFragment() {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        try {
            googleMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            e.printStackTrace();
        }


        // add markers
        if (locations != null) {
            for (UserLocation location : locations) {
                //add marker for each location
                googleMap.addMarker(new MarkerOptions()
                        .title(location.getLocationTitle())
                        .snippet(location.getLocationDescription())
                        .position(new LatLng(location.getLat(), location.getLng())));
            }
        }
    }

    private void initGoogleApiClient() {
        // Create an instance of GoogleAPIClient.
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void moveCamera(Location location) {
        //Log.d(MyApp.TAG, "move camera");
        if (this.googleMap != null) {
            // move camera to last known location
            CameraPosition cameraPosition = CameraPosition.builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .zoom(MAP_ZOOM)
                    .build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                    MAP_MOVE_CAMERA_DURATION, null);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            Location location = LocationServices.FusedLocationApi.getLastLocation(
                    googleApiClient);
            if(location != null){
                moveCamera(location);
            }

        } catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
