package com.example.seabattle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class PlacementOfShipsActivity extends AppCompatActivity implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener,
        View.OnDragListener{

    private  static final String TAG = "PlacementOfShips";
    UITableManager uiTableManager;
    Fleet myFleet;
//      widgets
    private View shipOf1View;
    private View shipOf2View;
    private View shipOf3View;
    private View shipOf4View;


    private TableLayout tableView;

    private View globalView;


    private GestureDetector mGestureDetector;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placement_of_ships);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        tableView = findViewById(R.id.layouttable_set_ships);
        globalView = findViewById(R.id.layout_ship_placement);

        mGestureDetector = new GestureDetector(this,this);

        myFleet = new Fleet();
        uiTableManager = new UITableManager(this,tableView, myFleet.getShipsGrid());
        myFleet.events.subscribe("update",uiTableManager);

        uiTableManager.InitiateTable(new Grid(10,10),tableView);


//        Arrays.fill(,true);

        shipOf1View = findViewById(R.id.image_ship_of1);
        shipOf2View = findViewById(R.id.image_ship_of2);
        shipOf3View = findViewById(R.id.image_ship_of3);
        shipOf4View = findViewById(R.id.image_ship_of4);//ships images

        shipOf1View.setOnTouchListener(this);
        shipOf2View.setOnTouchListener(this);
        shipOf3View.setOnTouchListener(this);
        shipOf4View.setOnTouchListener(this);// drag&drop for ships


        Button rotationButton = findViewById(R.id.button_rotation);
        rotationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    myFleet.RotateShip();
            }
        });

        Button autoPlacementButton = findViewById(R.id.button_auto_placement);
        autoPlacementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myFleet.AutoPlaceFleet();
            }
        });

        Button moveToBattleActivityButton = findViewById(R.id.button_start_battle);
        moveToBattleActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlacementOfShipsActivity.this, BattleActivity.class);
                Grid shipsGrid = myFleet.getShipsGrid();
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("keyShipsGrid", shipsGrid.getGrid());
                intent.putExtras(mBundle);
//                intent.putExtra("keyShipsGrid",shipsGrid.getGrid());
                startActivity(intent);
            }
        });


    }




    //region UI
    void PlaceShipOnDrop(DragEvent event, View v){
        int[] cell = uiTableManager.GetCellCoordsFromUITable(tableView,
                                                        new int[]{(int)event.getX(),(int)(event.getY())});

        boolean isUpperPart=false;
        if ((int)(event.getY())%60<30)isUpperPart=true;

        if (cell[0]==-1||cell[1]==-1)return;//if not inside table

        int length=0;

//                Log.d(TAG, "Coords: " + cell[0] +" : "+ cell[1]);
//                Log.d(TAG, "onDrag: ended. view: "+v.getId());

        if(v.getId()==shipOf1View.getId()) {
//            Log.d(TAG, "catter");
            length=1;
        }
        else if(v.getId()==shipOf2View.getId()){
//            Log.d(TAG, "missile boat");
            length=2;
        }
        else if(v.getId()==shipOf3View.getId()){
//            Log.d(TAG, "DESTROYER");
            length=3;
        }
        else if(v.getId()==shipOf4View.getId()){
//            Log.d(TAG, "THATS SOME HUGE ASS AIRCRAFTCARRIER");
            length=4;

        }
        myFleet.PlaceShip(cell,length);
//        PlaceShip(cell,length);

    }//Converts drop coords into grid coords // and place ship there


    boolean isInsideElement(int elemPosX,int elemPosY,int width,int height,int posX,int posY){
        if(elemPosX>posX||elemPosY>posY)return false;
        if(elemPosX+width<posX||elemPosY+height<posY)return false;

        return true;
    }// Checks if current dragging view is inside UI table
    void SetDrag(View view,int width,int height,MotionEvent e){
        int[] viewCoordinates = new int[2];
        view.getLocationOnScreen(viewCoordinates);

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);

        int topOffset = dm.heightPixels - globalView.getMeasuredHeight();
        int y=(int)e.getRawY()-topOffset+120;
//        Log.d(TAG,"offset"+topOffset);
//        Log.d(TAG,"Setting drag "+e.getRawX()+" : "+y);
//        Log.d(TAG,"view coords "+viewCoordinates[0]+" : "+(viewCoordinates[1]-topOffset+120));//ERROR second uncorrect
        if(!isInsideElement(viewCoordinates[0],(viewCoordinates[1]-topOffset+120),width,height,(int)e.getRawX(),y))return;


        View.DragShadowBuilder builder = new View.DragShadowBuilder(view);

        view.startDrag(null,
                builder,
                null,
                0);

        builder.getView().setOnDragListener(this);
    }// Create drag of view in Drag&Drop system

    //endregion

    //region Touch functions
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        int action = event.getAction();
        return true;
    }
        /*
        GestureDetector
         */
    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(TAG,"Action was onDown");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d(TAG,"Action was onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d(TAG,"Action was onSingleTap");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d(TAG,"Action was onScroll");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d(TAG,"Action was onLongPress");
//        SetDrag(shipOf1View,60,60,e);
        SetDrag(shipOf1View,60,60,e);
        SetDrag(shipOf2View,120,120,e);
        SetDrag(shipOf3View,180,180,e);
        SetDrag(shipOf4View,240,240,e);

    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(TAG,"Action was onFling");
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch(event.getAction()) {

            case DragEvent.ACTION_DRAG_STARTED:
                Log.d(TAG, "onDrag: drag started.");

                return true;

            case DragEvent.ACTION_DRAG_ENTERED:
                Log.d(TAG, "onDrag: drag entered.");
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                Log.d(TAG, "onDrag: current point: ( " + event.getX() + " , " + event.getY() + " )"  );

                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                Log.d(TAG, "onDrag: exited.");
                return true;

            case DragEvent.ACTION_DROP:

                Log.d(TAG, "onDrag: dropped.");

                return true;

            case DragEvent.ACTION_DRAG_ENDED:
//                event.getClipDescription();
//                Log.d(TAG, event.getClipDescription().toString());
                Log.d(TAG, "onDrag: ended.");

                PlaceShipOnDrop(event,v);

//                v.cancelDragAndDrop();

                return true;

            // An unknown action type was received.
            default:
                Log.e(TAG,"Unknown action type received by OnStartDragListener.");
                break;

        }
        return false;
    }
    //endregion
}