package com.example.vit.pinmyplace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.vit.pinmyplace.adapters.LocationsAdapter;
import com.example.vit.pinmyplace.models.User;
import com.example.vit.pinmyplace.models.UserLocation;
import com.example.vit.pinmyplace.utils.PrefUtils;
import com.facebook.login.LoginManager;

import org.parceler.Parcels;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    FloatingActionButton fab;
    User user;

    ListView lvLocations;
    LocationsAdapter adapter;

    List<UserLocation> userLocations;

    int selectedItemPosition = ListView.INVALID_POSITION;

    public static final int REQUEST_ADD_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkUser();
        initToolbar();
        initViews();
        setListeners();
        setupList();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(MyApp.TAG, "reqCode - " + requestCode + ", resCode = " + resultCode);
        switch (requestCode){
            case REQUEST_ADD_LOCATION:
                if(resultCode == Activity.RESULT_OK){
                    loadUserLocations();
                }
                break;
            default:
                break;
        }
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
                startActivityForResult(intent, REQUEST_ADD_LOCATION);
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

    private void setupList() {
        adapter = new LocationsAdapter(getBaseContext());
        lvLocations.setAdapter(adapter);
        unregisterForContextMenu(lvLocations);
        lvLocations.setOnItemLongClickListener(this);
    }

    private void loadUserLocations() {
        this.userLocations = UserLocation.find(UserLocation.class, "facebook_Id = ?", user.facebookId);

        if (userLocations != null) {
            /*
            for(UserLocation location : userLocations){
                Log.d(MyApp.TAG, location.toString());
            }
            */
            adapter.setLocations(userLocations);
        }
    }

    private void goToMap() {
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        intent.putExtra("locations", Parcels.wrap(userLocations));
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(MyApp.TAG, "onItemLongClick");
        this.selectedItemPosition = position;
        //view.setSelected(true);
        startSupportActionMode(modeCallBack);

        lvLocations.setItemChecked(selectedItemPosition, true);
        return true;
    }

    private ActionMode.Callback modeCallBack = new ActionMode.Callback() {

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public void onDestroyActionMode(ActionMode mode) {
            Log.d(MyApp.TAG, "onDestroyActionMode");
            lvLocations.setItemChecked(selectedItemPosition, false);
            mode = null;
        }

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            Log.d(MyApp.TAG, "onCreateActionMode");
            // TODO change selectedItemPosition to lvLocations.getCheckedItemPosition()
            if(selectedItemPosition != ListView.INVALID_POSITION){
                mode.setTitle(adapter.getItem(selectedItemPosition).getLocationTitle());
            }
            mode.getMenuInflater().inflate(R.menu.main_actions, menu);

            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_delete:
                    deleteLocation(selectedItemPosition);
                    break;
                default:
                    break;

            }
            mode.finish();
            return true;
        }
    };

    private void deleteLocation(int itemPosition){
        Log.d(MyApp.TAG, "Delete " + itemPosition);
        //delete location from db
        userLocations.get(itemPosition).delete();
        // remove from list
        adapter.remove(itemPosition);
    }

}
