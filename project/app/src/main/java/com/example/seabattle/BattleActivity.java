package com.example.seabattle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

public class BattleActivity extends AppCompatActivity {

    private  static final String TAG = "BATTLE_ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        Bundle mBundle = new Bundle();

        boolean[][] arrayReceived=null;
        Object[] objectArray = (Object[]) getIntent().getExtras().getSerializable("keyShipsGrid");
        if(objectArray!=null){
            arrayReceived = new boolean[objectArray.length][];
            for(int i=0;i<objectArray.length;i++){
                arrayReceived[i]=(boolean[]) objectArray[i];
            }
        }



        Grid shipsGrid = new Grid(arrayReceived);
        Log.d(TAG,"hello im a new window");
        shipsGrid.LogGrid();

//        shipsGrid.LogGrid();
    }



}