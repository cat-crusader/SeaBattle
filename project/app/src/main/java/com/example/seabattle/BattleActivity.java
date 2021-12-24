package com.example.seabattle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.TableLayout;

public class BattleActivity extends AppCompatActivity {

    private  static final String TAG = "BATTLE_ACTIVITY";
    UITableManager player1UITableManager;
    UITableManager player2UITableManager;

    Fleet player1Fleet;
    Fleet player2Fleet;

    private TableLayout player1tableView;
    private TableLayout player2tableView;

    private View globalView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // basics

        globalView = findViewById(R.id.layout_battle);

        player1tableView = findViewById(R.id.layouttable_player1);
        player2tableView = findViewById(R.id.layouttable_player2);




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

        player1Fleet = new Fleet(shipsGrid);
        player1UITableManager = new UITableManager(this,player1tableView, player1Fleet.getShipsGrid());
        player1Fleet.events.subscribe("update",player1UITableManager);

        player1UITableManager.InitiateTable(player1Fleet.getShipsGrid(),player1tableView);
        //player 1


        player2Fleet = new Fleet();
        player2Fleet.AutoPlaceFleet();
        player2UITableManager = new UITableManager(this,player2tableView, player2Fleet.getShipsGrid());
        player2Fleet.events.subscribe("update",player2UITableManager);

        player2UITableManager.InitiateTable(player2Fleet.getShipsGrid(),player2tableView);
        //player 2
    }



}