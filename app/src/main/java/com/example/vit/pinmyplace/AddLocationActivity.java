package com.example.vit.pinmyplace;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vit.pinmyplace.models.UserLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class AddLocationActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    FloatingActionButton fab;
    LinearLayout llLocation;

    TextInputLayout tilTitle;
    EditText etTitle;
    EditText etDescription;
    TextView tvLocation;

    GoogleApiClient googleApiClient;

    UserLocation userLocation;
    String facebookId = "0";

    boolean userPickedLocation = false;

    private static final int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        initToolbar();
        initViews();
        setListeners();
        initGoogleApiClient();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            facebookId = bundle.getString("facebookId", "0");
        }

        userLocation = new UserLocation(facebookId);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);
                    if(place != null){
                        userPickedLocation = true;
                        tvLocation.setText(place.getName());
                        userLocation.setLat(place.getLatLng().latitude);
                        userLocation.setLng(place.getLatLng().longitude);
                    }

                }
                break;
            default:
                break;
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        llLocation = (LinearLayout) findViewById(R.id.llLocation);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etDescription = (EditText) findViewById(R.id.etDescription);
        tilTitle = (TextInputLayout) findViewById(R.id.tilTitle);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
    }

    private void setListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(MyApp.TAG, "Title - " + etTitle.getText() + ", description - " + etDescription.getText());
                if(etTitle.getText().toString().isEmpty()){
                    tilTitle.setError(getString(R.string.title_error));
                } else {
                    tilTitle.setError(null);

                    // save in db
                    userLocation.setLocationTitle(etTitle.getText().toString());
                    userLocation.setLocationDescription(etDescription.getText().toString());
                    userLocation.save();
                    finish();
                }
            }
        });

        llLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(AddLocationActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.d(MyApp.TAG, e.toString());
                } catch (GooglePlayServicesRepairableException e2) {
                    Log.d(MyApp.TAG, e2.toString());
                }

            }
        });

    }

    private void initGoogleApiClient(){
        // Create an instance of GoogleAPIClient.
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(MyApp.TAG, "onConnected()");
        try {
            Location location = LocationServices.FusedLocationApi.getLastLocation(
                    googleApiClient);
            if(location != null){
                Log.d(MyApp.TAG, "lastknown - " + location.getLatitude() + ", " + location.getLongitude());
                if(!userPickedLocation){
                    userLocation.setLat(location.getLatitude());
                    userLocation.setLng(location.getLongitude());
                }
            }

        } catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(MyApp.TAG, "onConnectionSuspended()");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(MyApp.TAG, "onConnectionFailed()");
    }
}
