package com.example.seabattle;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class UIManager {

    private static UIManager instance;
    private static Context context;

    private UIManager(Context _context){
        context=_context;
    };

    public static UIManager getInstance(Context _context){
        if(instance == null){
            instance=new UIManager(_context);
        }
        return instance;
    }//Singleton

    public void InitiateTable(Grid grid,TableLayout tableLayout){
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
    }// Initiate UI table// currently always initiate 10x10 table

    public void UpdateTableByValue(Grid grid,TableLayout tableLayout, int shipSpriteId, int emptySpriteId){

        boolean[][]table = grid.getGrid();

        for (int i = 0; i < grid.getSizeX(); i++) {
            for (int j = 0; j < grid.getSizeY(); j++) {

                if(table[i][j]==true) {
                    RedactCellElement(tableLayout,true, i, j,shipSpriteId,emptySpriteId);
//                    Log.d(TAG,"Cell: "+i+" : "+j+" are ship");
                }
                else RedactCellElement(tableLayout,false,i,j,shipSpriteId,emptySpriteId);
            }
        }
    }// refresh UI table

    public void RedactCellElement(TableLayout tableLayout,boolean cellValue,int posX,int posY,int trueSpriteId,int falseSpriteId){
        View row = ((ViewGroup) tableLayout).getChildAt(posY);
        ImageView image =(ImageView) ((ViewGroup) row).getChildAt(posX);

        if(cellValue==true)image.setImageResource(trueSpriteId);
        else image.setImageResource(falseSpriteId);
    }// Set image of cell in UI table

    public int[] GetCellCoordsFromUITable(TableLayout tableLayout,int [] coordinates){

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
