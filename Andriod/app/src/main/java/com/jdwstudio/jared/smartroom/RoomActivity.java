package com.jdwstudio.jared.smartroom;

import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RoomActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TCPClient mTcpClient;
    private connectTask cTask;
    Button TvPower;
    TextView ConnectionInfo, Warning;
    String host = "pi.jaredwines.com";
    int port = 8000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ConnectionInfo = (TextView) findViewById(R.id.connectionInfo);
        Warning = (TextView) findViewById(R.id.warning);

        cTask = new connectTask();
        cTask.execute();
    }
    public class connectTask extends AsyncTask<String, String, TCPClient> {

        @Override
        protected TCPClient doInBackground(String... message) {

            //we create a TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.TcpClientListener() {
                @Override
                public void onMessageReceived(TCPClient tcpClient, String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }

                @Override
                public void onConnecting(TCPClient tcpClient, String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }

                @Override
                public void onConnected(TCPClient tcpClient, String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }

                @Override
                public void onDisconnected(TCPClient tcpClient, String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }

            });
            mTcpClient.start(host, port);

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            /*
            2 = connecting to server
            3 = connected to server
            4 = disconnected from server
             */

            //Connected to server
            if (values[0].equals("3")) {
                //Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
                ConnectionInfo.setTextColor(Color.GREEN);
                ConnectionInfo.setText("Connected");
                Warning.setText("");
            }
            //Disconnected from server
            else if (values[0].equals("4")) {
                //Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_LONG).show();
                if(isCorrectSSID()) {
                    ConnectionInfo.setTextColor(Color.RED);
                    ConnectionInfo.setText("Disconnected");
                    Warning.setText("");
                }
                else {
                    ConnectionInfo.setTextColor(Color.RED);
                    ConnectionInfo.setText("Error!!!");
                    Warning.setText("You are not connected to network \"Wines\"!");
                }
            }
        }
    }

    private void connect() {
        if(isCorrectSSID())
        {
            if (mTcpClient.isConnected())
                Toast.makeText(getApplicationContext(), "You are already connected to the server.", Toast.LENGTH_LONG).show();
            else {
                mTcpClient.start(host, port);
            }
        }
        else
            Toast.makeText(getApplicationContext(), "You are not connected to network \"Wines\"!", Toast.LENGTH_LONG).show();
    }

    private boolean isCorrectSSID()
    {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSSID().equals("\"Wines\""))
            return true;
        else
            //return false;
            return true;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.room, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_connect) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            //Is already in RoomActivity.class
            //Intent i = new Intent(getApplicationContext(),RoomActivity.class);
            //startActivity(i);
        } else if (id == R.id.nav_about) {
            Intent i = new Intent(getApplicationContext(),AboutActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
