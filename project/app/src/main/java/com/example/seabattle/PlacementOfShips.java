package com.example.seabattle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlacementOfShips extends AppCompatActivity implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener,
        View.OnDragListener{

    private  static final String TAG = "PlacementOfShips";

//      widgets
    private View shipOf1View;
    private View shipOf2View;
    private View shipOf3View;
    private View shipOf4View;

    private TableLayout tableView;

    private View globalView;


//      vars
    private Grid placementGrid;
    private GestureDetector mGestureDetector;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placement_of_ships);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        shipOf1View = findViewById(R.id.image_ship_of1);
        shipOf2View = findViewById(R.id.image_ship_of2);
        shipOf3View = findViewById(R.id.image_ship_of3);
        shipOf4View = findViewById(R.id.image_ship_of4);//ships images

        shipOf1View.setOnTouchListener(this);
        shipOf2View.setOnTouchListener(this);
        shipOf3View.setOnTouchListener(this);
        shipOf4View.setOnTouchListener(this);// drag&drop for ships

        mGestureDetector = new GestureDetector(this,this);
        tableView = findViewById(R.id.layouttable_set_ships);
        globalView = findViewById(R.id.layout_ship_placement);

        Button testButton = findViewById(R.id.test_button2);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] viewCoordinates = new int[2];
                shipOf1View.getLocationInWindow(viewCoordinates);
                Log.d(TAG,"view coords of ship2 "+viewCoordinates[0]+" : "+viewCoordinates[1]);

            }
        });

        InitiateTable(new Grid(10,10));
    }
    void PlaceShip(DragEvent event,int size){
        int[] cell = GetTableElement(new int[]{(int)event.getX(),(int)(event.getY())});//center of ship

        Log.d(TAG, "Coords: " + cell[0] +" : "+ cell[1]);
        Log.d(TAG, "onDrag: ended.");
        RedactCellElement(true,cell[0],cell[1]);
    }
    boolean isInsideElement(int elemPosX,int elemPosY,int width,int height,int posX,int posY){
        if(elemPosX>posX||elemPosY>posY)return false;
        if(elemPosX+width<posX||elemPosY+height<posY)return false;

        return true;
    }
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
    }
    //region Table
    public void InitiateTable(Grid grid){
        for (int i = 0; i < 10; i++) {

            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < 10; j++) {

                ImageView view = new ImageView(this);
                view.setImageResource(R.drawable.empty_cell_sprite);
                tr.addView(view);
                view.getLayoutParams().height=60;
                view.getLayoutParams().width=60;
            }
            tableView.addView(tr);
        }
    }
    public void RedactCellElement(boolean cellValue,int posX,int posY){
        View row = ((ViewGroup) tableView).getChildAt(posY);
        ImageView image =(ImageView) ((ViewGroup) row).getChildAt(posX);

        image.setImageResource(R.drawable.ship_sprite);
    }
    public int[] GetTableElement(int [] coordinates){

        int[] tableCoordinates = new int[2];
        tableView.getLocationOnScreen(tableCoordinates);
        //Log.d(TAG, "fucking android studio" + tableCoordinates[0] + " : "+ tableCoordinates[1]);
        int[] cellTableCoordinates = new int[2];
        int cellSize = 60;
        int tableSize = cellSize*10;
        if(tableCoordinates[0]>coordinates[0]||tableCoordinates[1]>coordinates[1])return new int[]{-1,-1};
        if(tableCoordinates[0]+tableSize<coordinates[0]||tableCoordinates[1]+tableSize< coordinates[1])return new int[]{-1,-1};

        coordinates[0]-=tableCoordinates[0];
        coordinates[1]-=tableCoordinates[1];
        cellTableCoordinates[0] = coordinates[0]/cellSize;
        cellTableCoordinates[1] = coordinates[1]/cellSize;

        return cellTableCoordinates;
    }

    //endregion

    //region Touch functions
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        int action = event.getAction();
//        switch (action){
//            case (MotionEvent.ACTION_MOVE):
//                Log.d(TAG,"onTouch:"+event.getX() +" , "+event.getY());
//                break;
//            default:
//                break;
//        }

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
                int[] cell = GetTableElement(new int[]{(int)event.getX(),(int)(event.getY())});
                boolean isUpperPart=false;
                if ((int)(event.getY())%60<30)isUpperPart=true;

                if (cell[0]==-1||cell[1]==-1)return true;//if not inside table

                int length=0;

//                Log.d(TAG, "Coords: " + cell[0] +" : "+ cell[1]);
//                Log.d(TAG, "onDrag: ended. view: "+v.getId());

                if(v.getId()==shipOf1View.getId()) {
                    Log.d(TAG, "catter");
                    length=1;
                }
                if(v.getId()==shipOf2View.getId()){
                    Log.d(TAG, "missile boat");
                    length=2;
                }
                if(v.getId()==shipOf3View.getId()){
                    Log.d(TAG, "DESTROYER");
                    length=3;
                }
                if(v.getId()==shipOf4View.getId()){
                    Log.d(TAG, "THATS SOME HUGE ASS AIRCRAFTCARRIER");
                    length=4;
                }
                Log.d(TAG, "Length of ship: "+length);
                Ship newShip = new Ship(length);
                newShip.Place(cell,isUpperPart);
                ArrayList<int[]> shipList = newShip.getCorpus();
                for (int[] part: shipList
                     ) {

                    RedactCellElement(true,part[0],part[1]);
                }
                //RedactCellElement(true,cell[0],cell[1]);

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