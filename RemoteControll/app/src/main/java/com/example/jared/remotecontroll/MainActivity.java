package com.example.jared.remotecontroll;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import static com.example.jared.remotecontroll.R.id.tv_remote;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickTvRemote(View view) {
        Intent intent = new Intent(this, TvRemote.class);
        startActivity(intent);
    }

}
