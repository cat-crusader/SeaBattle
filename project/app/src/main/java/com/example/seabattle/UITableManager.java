package com.example.seabattle;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class UITableManager implements EventListener{

//    private static UITableManager instance;
    private  static final String TAG = "SHIP";
    private Context context;
    private TableLayout tableView;
    private Grid shipsGrid;
    private Grid hitGrid;

     UITableManager(Context _context,TableLayout _tableView, Grid _shipsGrid,Grid _hitGrid){
        context=_context;
         tableView =_tableView;
         shipsGrid=_shipsGrid;
         hitGrid=_hitGrid;
    };

//    public static UITableManager getInstance(Context _context){
//        if(instance == null){
//            instance=new UITableManager(_context);
//        }
//        return instance;
//    }//Singleton
    @Override
    public void update(String eventType){
        Log.d(TAG,"UI updates");
        if(eventType=="placing_update") {
            updateTableByValue(shipsGrid, tableView, R.drawable.ship_sprite, true);
            updateTableByValue(shipsGrid, tableView, R.drawable.empty_cell_sprite, false);
        }
        else if(eventType =="battle_update"){

            updateTableByValue(hitGrid, tableView, R.drawable.shadow_cell_sprite, true);
            updateTableByValue(hitGrid, tableView, R.drawable.fog_of_war_cell, false);
            updateTableByValue(hitGrid.AND(shipsGrid), tableView, R.drawable.hitted_ship_cell, true);
        }
    }

    public void initiateTable(Grid grid, TableLayout tableLayout){// TODO: 24.12.2021 ! Currently works only with 10 x 10 table !
        for (int i = 0; i < 10; i++) {

            TableRow tr = new TableRow(context);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < 10; j++) {

                ImageView view = new ImageView(context);
                view.setImageResource(R.drawable.empty_cell_sprite);
                tr.addView(view);
                view.getLayoutParams().height=60;
                view.getLayoutParams().width=60;
            }
            tableLayout.addView(tr);
        }
//        UpdateTableByValue(grid,tableLayout,R.drawable.ship_sprite,R.drawable.empty_cell_sprite);
    }// Initiate UI table// currently always initiate 10x10 table

    public void updateTableByValue(Grid grid, TableLayout tableLayout, int shipSpriteId, boolean value){

        boolean[][]table = grid.getGrid();

        for (int i = 0; i < grid.getSizeX(); i++) {
            for (int j = 0; j < grid.getSizeY(); j++) {

                if(table[i][j]==value) {
                    redactCellElement(tableLayout,true, i, j,shipSpriteId);
//                    Log.d(TAG,"Cell: "+i+" : "+j+" are ship");
                }
                //else RedactCellElement(tableLayout,false,i,j,shipSpriteId);
            }
        }
    }// refresh UI table

    public void redactCellElement(TableLayout tableLayout, boolean cellValue, int posX, int posY, int trueSpriteId){
        View row = ((ViewGroup) tableLayout).getChildAt(posY);
        ImageView image =(ImageView) ((ViewGroup) row).getChildAt(posX);

        if(cellValue==true)image.setImageResource(trueSpriteId);
        //else image.setImageResource(falseSpriteId);
    }// Set image of cell in UI table

    public int[] getCellCoordsFromUITable(TableLayout tableLayout, int [] coordinates){

        int[] tableCoordinates = new int[2];
        tableLayout.getLocationOnScreen(tableCoordinates);
        //Log.d(TAG, "fucking android studio" + tableCoordinates[0] + " : "+ tableCoordinates[1]);
        int[] cellTableCoordinates = new int[2];
        int cellSize = 60;
        int tableSize = cellSize*10;

        if(tableCoordinates[0]>coordinates[0]||tableCoordinates[1]>coordinates[1])return new int[]{-1,-1};
        if(tableCoordinates[0]+tableSize<coordinates[0]||tableCoordinates[1]+tableSize< coordinates[1])return new int[]{-1,-1};
        //if outside of table

        coordinates[0]-=tableCoordinates[0];
        coordinates[1]-=tableCoordinates[1];
        cellTableCoordinates[0] = coordinates[0]/cellSize;
        cellTableCoordinates[1] = coordinates[1]/cellSize;

        return cellTableCoordinates;
    }// return cell coordinates from UI table


}
