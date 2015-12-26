package com.example.vit.pinmyplace;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.vit.pinmyplace.adapters.LocationsAdapter;
import com.example.vit.pinmyplace.models.User;
import com.example.vit.pinmyplace.models.UserLocation;
import com.example.vit.pinmyplace.utils.PrefUtils;
import com.facebook.login.LoginManager;

import org.parceler.Parcels;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    User user;

    ListView lvLocations;
    LocationsAdapter adapter;

    List<UserLocation> userLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkUser();
        initToolbar();
        initViews();
        setListeners();
        setupList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadUserLocations();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Log.d(MyApp.TAG, "Logout");
                PrefUtils.clearCurrentUser(getBaseContext());
                LoginManager.getInstance().logOut();
                goToLogin();
                break;
            case R.id.action_show_map:
                Log.d(MyApp.TAG, "Show Map");
                goToMap();
                break;
            default:
                break;
        }
        return true;
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initViews() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        lvLocations = (ListView) findViewById(R.id.lvLocations);
    }

    private void setListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start new location activity
                Intent intent = new Intent(MainActivity.this, AddLocationActivity.class);
                intent.putExtra("facebookId", user.facebookId);
                startActivity(intent);
            }
        });
    }

    private void checkUser() {
        this.user = PrefUtils.getCurrentUser(getBaseContext());
        if (this.user == null) {
            // go to login page
            goToLogin();
        }
    }

    private void goToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void setupList(){
        adapter = new LocationsAdapter(getBaseContext());
        lvLocations.setAdapter(adapter);
    }

    private void loadUserLocations(){
        this.userLocations = UserLocation.find(UserLocation.class, "facebook_Id = ?", user.facebookId);

        if(userLocations != null){
            /*
            for(UserLocation location : userLocations){
                Log.d(MyApp.TAG, location.toString());
            }
            */
            adapter.setLocations(userLocations);
        }
    }

    private void goToMap(){
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        intent.putExtra("locations", Parcels.wrap(userLocations));
        startActivity(intent);
    }

}
