package com.example.seabattle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableLayout;

public class BattleActivity extends AppCompatActivity implements View.OnTouchListener {

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
        // To retrieve object in second Activity
//        player1Fleet = (Fleet) getIntent().getSerializableExtra("keyFleet");
//        Stack<Ship> shipStack = ((Stack<Ship>) getIntent().getSerializableExtra("keyShipsStack"));
//        player1Fleet.setShipsStack
        player1UITableManager = new UITableManager(this,player1tableView, player1Fleet.getShipsGrid(),player1Fleet.getHitGrid());
        player1Fleet.events.subscribe("battle_update",player1UITableManager);

        player1UITableManager.initiateTable(player1Fleet.getShipsGrid(),player1tableView);
        //player 1


        player2Fleet = new Fleet();
        player2Fleet.autoPlaceFleet();
        player2UITableManager = new UITableManager(this,player2tableView, player2Fleet.getShipsGrid(),player2Fleet.getHitGrid());
        player2Fleet.events.subscribe("battle_update",player2UITableManager);

        player2UITableManager.initiateTable(player2Fleet.getShipsGrid(),player2tableView);
        //player 2

        player2tableView.setOnTouchListener(this);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int [] arr = player2UITableManager.getCellCoordsFromUITable(player2tableView,new int[]{(int)event.getRawX(),(int)event.getRawY()});
        Log.d(TAG,"coords: "+arr[0]+" ; "+ arr[1]);
        playerAttack(player2Fleet,arr[0],arr[1]);
        return true;
    }
    public void playerAttack(Fleet fleet, int posX, int posY){
        if(fleet.takeHit(posX,posY)){

            randomAttack(player1Fleet);
        }
    }
    public void randomAttack(Fleet fleet){
        int randomXPos,randomYPos;
        do {
            randomYPos = 0 + (int) (Math.random() * 10);
            randomXPos = 0 + (int) (Math.random() * 10);
        }
        while(!fleet.takeHit(randomXPos,randomYPos));

    }
}