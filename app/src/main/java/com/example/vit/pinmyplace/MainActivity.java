package com.example.vit.pinmyplace;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vit.pinmyplace.utils.PrefUtils;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    User user;

    TextView tvUserName;
    ImageView ivUserImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkUser();
        initToolbar();
        initViews();
        setListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Picasso.with(getBaseContext())
                .load("https://graph.facebook.com/" + user.facebookId + "/picture?type=large")
                .into(ivUserImage);

        tvUserName.setText(user.name);
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
                return true;
            default:
                return true;
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initViews() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        ivUserImage = (ImageView) findViewById(R.id.ivUserImage);
    }

    private void setListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

}
