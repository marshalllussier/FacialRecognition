package com.autozone.facialrecognition;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.autozone.facialrecognition.fragments.AlternateloginFragment;
import com.autozone.facialrecognition.fragments.CreateprofileFragment;
import com.autozone.facialrecognition.fragments.LoginhistoryFragment;
import com.autozone.facialrecognition.fragments.ScanfaceFragment;
import com.autozone.facialrecognition.fragments.SettingsFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private Toolbar toolbar;
    public NavigationView navigationView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScanfaceFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_scan_face);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_scan_face:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScanfaceFragment()).commit();
                break;
            case R.id.nav_alternate_login:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AlternateloginFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                break;
            case R.id.nav_login_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginhistoryFragment()).commit();
                break;
            case R.id.nav_create_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreateprofileFragment()).commit();
                break;
            case R.id.nav_support:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + getString(R.string.support_number)));
                startActivity(intent);
                break;
            case R.id.nav_logout:
//                set_user_logged_in(false);   // This needs menu fixed first, currently it must be manually reset.
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void set_user_logged_in(boolean loggedOn) {
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_login_history).setVisible(loggedOn);
        menu.findItem(R.id.nav_settings).setVisible(loggedOn);
        menu.findItem(R.id.nav_create_profile).setVisible(loggedOn);
        menu.findItem(R.id.nav_logout).setVisible(loggedOn);

        menu.findItem(R.id.nav_alternate_login).setVisible(!loggedOn);
    }
}
