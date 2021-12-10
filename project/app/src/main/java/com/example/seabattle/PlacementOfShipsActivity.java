package com.example.seabattle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Build;
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
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class PlacementOfShipsActivity extends AppCompatActivity implements
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

    private Grid shipsGrid;
    private Grid shadowGrid;
    private GestureDetector mGestureDetector;

    private int[] shipsAmmount ={4,3,2,1};
    private int shipsCount=0;
    private Stack<Ship> shipsStack = new Stack<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placement_of_ships);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        shipsGrid = new Grid(10,10);
        shadowGrid = new Grid(10,10);
        shadowGrid.fill(true);
        shipsGrid.fill(false);
//        Arrays.fill(,true);

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

        Button rotationButton = findViewById(R.id.button_rotation);
        rotationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int[] viewCoordinates = new int[2];
//                shipOf1View.getLocationInWindow(viewCoordinates);
//                Log.d(TAG,"view coords of ship2 "+viewCoordinates[0]+" : "+viewCoordinates[1]);
                    RotateShip();
            }
        });
        Button autoPlacementButton = findViewById(R.id.button_auto_placement);
        autoPlacementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoPlaceFleet();

            }
        });

        InitiateTable(new Grid(10,10));
    }

    boolean CanBePlaced(Ship ship){
        ArrayList<int[]> shipPart = ship.getCorpus();
        for (int[] part: shipPart
        ) {
            if(part[0]>9||part[1]>9)return false;
            if(part[0]<0||part[1]<0)return false;//out of grid

            if(!shadowGrid.getGrid()[part[0]][part[1]])return false;

        }


        return true;
    }
    boolean InBounds(int[] cell){

            if(cell[0]>9||cell[1]>9)return false;
            if(cell[0]<0||cell[1]<0)return false;//out of grid

        return true;
    }
    public void AutoPlaceFleet(){
        for (int t = 0; t < shipsAmmount.length; t++) {
            int shipsInType = shipsAmmount[t];
            for (int i = 0; i < shipsInType; i++) {
                AutoPlaceShip(t+1);
            }


        }
    }
    public void AutoPlaceShip(int shipLength){
        int shipAmmount = shipsStack.size();
        while (shipAmmount==shipsStack.size()&&shipsAmmount[shipLength-1]>0){
            Random r = new Random();
            boolean rotation = r.nextBoolean();
            int randomYPos = (shipLength-1) + (int) (Math.random() * 10);
            int randomXPos = 0 + (int) (Math.random() * (11-shipLength));
            PlaceShip(new int[]{randomXPos,randomYPos},shipLength);
        }



    }
    public void RotateShip(){

        Ship rotatedShip = shipsStack.peek();
        rotatedShip.Rotate();

        if(!CanBePlaced(rotatedShip))return;
        rotatedShip.Rotate();
        ArrayList<int[]> rotatedShipList = rotatedShip.getCorpus();




        shipsGrid.LogGrid();
        for (int[] part: rotatedShipList//deleting old version
        ) {
            shipsGrid.SetCell(part[0],part[1],false);
            Log.d(TAG,"deleting: "+part[0]+":"+part[1]);
            //RedactCellElement(true,part[0],part[1]);
        }
        rotatedShip.Rotate();
        rotatedShipList = rotatedShip.getCorpus();
//        shipsGrid.LogGrid();
        for (int[] part: rotatedShipList//setting rotated version
        ) {
            shipsGrid.SetCell(part[0],part[1],true);
            //RedactCellElement(true,part[0],part[1]);
        }
        shipsStack.pop();
        shipsStack.add(rotatedShip);

        UpdateTable(shipsGrid,R.drawable.ship_sprite,R.drawable.empty_cell_sprite);
//        shipsGrid.LogGrid();
    }
    void PlaceShipOnDrag(DragEvent event, View v){
        int[] cell = GetTableElement(new int[]{(int)event.getX(),(int)(event.getY())});
        boolean isUpperPart=false;
        if ((int)(event.getY())%60<30)isUpperPart=true;

        if (cell[0]==-1||cell[1]==-1)return;//if not inside table

        int length=0;

//                Log.d(TAG, "Coords: " + cell[0] +" : "+ cell[1]);
//                Log.d(TAG, "onDrag: ended. view: "+v.getId());

        if(v.getId()==shipOf1View.getId()) {
            Log.d(TAG, "catter");
            length=1;
        }
        else if(v.getId()==shipOf2View.getId()){
            Log.d(TAG, "missile boat");
            length=2;
        }
        else if(v.getId()==shipOf3View.getId()){
            Log.d(TAG, "DESTROYER");
            length=3;
        }
        else if(v.getId()==shipOf4View.getId()){
            Log.d(TAG, "THATS SOME HUGE ASS AIRCRAFTCARRIER");
            length=4;

        }

        PlaceShip(cell,length);

    }
    void PlaceShip(int[] cell,int length){
        Ship newShip = new Ship(length);
        newShip.Place(cell);


        if(!shipsStack.empty()){//create shadow of previous ship
            ArrayList<int[]> shadowList = shipsStack.peek().getShadow();
            for (int[] part: shadowList
            ) {
//            if(InBounds(part))
                if(InBounds(part))shadowGrid.SetCell(part[0],part[1],false);
                //RedactCellElement(true,part[0],part[1]);
            }}

        if(!CanBePlaced(newShip))return;
        if(shipsAmmount[length-1]<=0)return;



        shipsStack.add(newShip);
        shipsAmmount[length-1]--;
        ArrayList<int[]> shipList = shipsStack.peek().getCorpus();
        for (int[] part: shipList
        ) {
            shipsGrid.SetCell(part[0],part[1],true);
            //RedactCellElement(true,part[0],part[1]);
        }

        UpdateTable(shadowGrid,R.drawable.empty_cell_sprite,R.drawable.shadow_cell_sprite);
        UpdateTable(shipsGrid,R.drawable.ship_sprite,R.drawable.empty_cell_sprite);
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
    public void UpdateTable(Grid grid,int shipSpriteId,int emptySpriteId){
        boolean[][]table = grid.getGrid();
//        Log.d(TAG,"Update Table ");
//        Log.d(TAG,"current amount of ships: "+shipsStack.size());
        for (int i = 0; i < grid.getSizeX(); i++) {
            for (int j = 0; j < grid.getSizeY(); j++) {

                if(table[i][j]==true) {
                    RedactCellElement(true, i, j,shipSpriteId,emptySpriteId);
//                    Log.d(TAG,"Cell: "+i+" : "+j+" are ship");
                }
                else RedactCellElement(false,i,j,shipSpriteId,emptySpriteId);
            }
        }
        shadowGrid.LogGrid();

    }
    public void RedactCellElement(boolean cellValue,int posX,int posY,int trueSpriteId,int falseSpriteId){
        View row = ((ViewGroup) tableView).getChildAt(posY);
        ImageView image =(ImageView) ((ViewGroup) row).getChildAt(posX);

        if(cellValue==true)image.setImageResource(trueSpriteId);
        else image.setImageResource(falseSpriteId);
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

                PlaceShipOnDrag(event,v);

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