package com.example.jared.remotecontroll;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class TvRemote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tv_remote);
    }
    public void onClickPower(View view) {
        Server tvPower = new Server("cd Arduino/;python tv_power.py");
        tvPower.serverCommand();
        //Toast.makeText(getApplicationContext(), String.valueOf(7), Toast.LENGTH_LONG).show();

    }

    public void onClickVolumeUp(View view) {
        Server tvVolumeUp = new Server("cd Arduino/;python tv_volume_up.py");
        tvVolumeUp.serverCommand();
        //Toast.makeText(getApplicationContext(), String.valueOf(7), Toast.LENGTH_LONG).show();

    }

    public void onClickVolumeDown(View view) {
        Server tvVolumeDown = new Server("cd Arduino/;python tv_volume_down.py");
        tvVolumeDown.serverCommand();
        //Toast.makeText(getApplicationContext(), String.valueOf(7), Toast.LENGTH_LONG).show();

    }
}
