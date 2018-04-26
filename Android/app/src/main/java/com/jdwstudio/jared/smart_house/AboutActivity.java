package com.jdwstudio.jared.smart_house;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button PlayStore, Github, Linkedin, PersonalSite, Resume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        PersonalSite = (Button) findViewById(R.id.personalSite);
        PlayStore = (Button) findViewById(R.id.playStore);
        Github = (Button) findViewById(R.id.github);
        Linkedin = (Button) findViewById(R.id.linkedin);
        Resume = (Button) findViewById(R.id.resume);

        PersonalSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl("http://www.jaredwines.com/");
            }
        });

        PlayStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl("https://play.google.com/store/apps/developer?id=Jared%20Wines&hl=en");
            }
        });

        Github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl("https://github.com/jaredwines");
            }
        });

        Linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl("https://www.linkedin.com/in/jaredwines/");
            }
        });

        Resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl("https://gdurl.com/WkLJ");
            }
        });

    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_gate_opener) {
            Intent i = new Intent(getApplicationContext(),GateOpenerActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_garage_opener) {
            Intent i = new Intent(getApplicationContext(),GarageOpenerActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_room) {
            Intent i = new Intent(getApplicationContext(),RoomActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_about) {
            //Is already in AboutActivity.class
            //Intent i = new Intent(getApplicationContext(),AboutActivity.class);
            //startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
