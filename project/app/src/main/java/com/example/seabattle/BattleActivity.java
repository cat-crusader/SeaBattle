package com.example.seabattle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class BattleActivity extends AppCompatActivity {

        Grid gridPlayer1;
        Grid gridPlayer2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        gridPlayer1 = new Grid(10,10);
        gridPlayer2 = new Grid(10,10);
    }



}