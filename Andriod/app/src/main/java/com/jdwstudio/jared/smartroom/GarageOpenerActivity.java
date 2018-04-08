package com.jdwstudio.jared.smartroom;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GarageOpenerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TCPClient mTcpClient;
    private connectTask cTask;
    Button One, Two, Three, Four, Five, Six, Seven, Eight, Nine, Zero, Clear, Enter;
    TextView PassCodeScreen, ConnectionInfo, Warning;
    String passCode = "";
    Boolean muteFlag = false;
    String host = "pi.jaredwines.com";
    int port = 8000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_opener);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //call the widgets
        One = (Button) findViewById(R.id.one);
        Two = (Button) findViewById(R.id.two);
        Three = (Button) findViewById(R.id.three);
        Four = (Button) findViewById(R.id.four);
        Five = (Button) findViewById(R.id.five);
        Six = (Button) findViewById(R.id.six);
        Seven = (Button) findViewById(R.id.seven);
        Eight = (Button) findViewById(R.id.eight);
        Nine = (Button) findViewById(R.id.nine);
        Zero = (Button) findViewById(R.id.zero);
        Clear = (Button) findViewById(R.id.clear);
        Enter = (Button) findViewById(R.id.enter);
        PassCodeScreen = (TextView) findViewById(R.id.passCodeScreen);
        ConnectionInfo = (TextView) findViewById(R.id.connectionInfo);
        Warning = (TextView) findViewById(R.id.warning);


        cTask = new connectTask();
        cTask.execute();

        numKeyPadOnClick();

        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passCode = "";
                PassCodeScreen.setText("****");
            }
        });


        Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passCode.length() == 4)
                    //Sends the passCode to the server
                    if (mTcpClient.isConnected()) {
                        //sends the message to the server
                        if (mTcpClient != null) {
                            mTcpClient.sendMessage("garage " + passCode);
                        }
                        passCode = "";
                        PassCodeScreen.setText("****");
                    } else {
                        Toast.makeText(getApplicationContext(), "Error! Try pressing \"CONNECT\" and make sure your connected to wifi named \"Wines\".", Toast.LENGTH_LONG).show();
                    }
                else
                    Toast.makeText(getApplicationContext(), "Passcode must be 4 digits long!", Toast.LENGTH_LONG).show();
            }
        });

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
            0 = Passcode was invalid
            1 = Passcode was valid
            2 = connecting to server
            3 = connected to server
            4 = disconnected from server
             */

            //Passcode was invalid
            if (values[0].equals("0")) {
                Toast.makeText(getApplicationContext(), "Incorrect, try again!!! :(", Toast.LENGTH_LONG).show();
                if (!muteFlag) {
                    MediaPlayer wrongCardSound = MediaPlayer.create(getApplicationContext(), R.raw.error);
                    wrongCardSound.start();
                }
            }
            //Passcode was valid
            else if (values[0].equals("1")) {
                Toast.makeText(getApplicationContext(), "Correct!!!", Toast.LENGTH_LONG).show();
                if (!muteFlag) {
                    MediaPlayer mushroomSound = MediaPlayer.create(getApplicationContext(), R.raw.secret);
                    mushroomSound.start();
                }
            }
            //Connecting to server
            else if (values[0].equals("2")) {
                //Toast.makeText(getApplicationContext(), "Connecting", Toast.LENGTH_LONG).show();
                if(isCorrectSSID()) {
                    ConnectionInfo.setTextColor(Color.WHITE);
                    ConnectionInfo.setText("Connecting");
                    Warning.setText("");
                }
                else {
                    ConnectionInfo.setTextColor(Color.RED);
                    ConnectionInfo.setText("Error!!!");
                    Warning.setText("You are not connected to network \"Wines\"!");
                }

            }
            //Connected to server
            else if (values[0].equals("3")) {
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

    private void mute() {
        if (muteFlag) {
            muteFlag = false;
            //Mute.setPaintFlags(Mute.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        } else if (!muteFlag) {
            muteFlag = true;
            //Mute.setPaintFlags(Mute.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    private void addToPassCode(String userInput) {
        if (passCode.length() <= 3) {
            passCode += userInput;
            PassCodeScreen.setText(passCode);
        } else
            Toast.makeText(getApplicationContext(), "4 digits have already been entered. Either press ENTER or CLEAR", Toast.LENGTH_LONG).show();
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
        getMenuInflater().inflate(R.menu.gate_opener, menu);
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
            connect();
            return true;
        } else if (id == R.id.action_mute) {
            mute();
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
            //Is already in GarageOpenerActivity.class
            //Intent i = new Intent(getApplicationContext(),GarageOpenerActivity.class);
            //startActivity(i);
        } else if (id == R.id.nav_room) {
            Intent i = new Intent(getApplicationContext(),RoomActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_about) {
            Intent i = new Intent(getApplicationContext(),AboutActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onPause() {
        super.onPause();
        mTcpClient.stop();
    }

    @Override
    protected void onResume() {
        mTcpClient.start(host, port);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void numKeyPadOnClick() {
        One.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToPassCode("1");
            }
        });

        Two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToPassCode("2");
            }
        });

        Three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToPassCode("3");
            }
        });

        Four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToPassCode("4");
            }
        });

        Five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToPassCode("5");
            }
        });

        Six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToPassCode("6");
            }
        });

        Seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToPassCode("7");
            }
        });

        Eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToPassCode("8");
            }
        });

        Nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToPassCode("9");
            }
        });

        Zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToPassCode("0");
            }
        });
    }
}