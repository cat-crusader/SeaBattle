package com.example.seabattle;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;


public class Ship implements Serializable {
    private  static final String TAG = "SHIP";
    private int length;
    private int[]rootCell;
    private ArrayList<int[]> corpus;
    private ArrayList<int[]> shadow;
    private boolean rotatedVertically;

    public Ship(int len){
        length=len;
        rootCell=new int[]{-1,-1};
        corpus = new ArrayList<>();
        shadow = new ArrayList<>();
    }

    public void Rotate(){
//        Log.d(TAG, "ship gets rotated"+ rotatedVertically);
        rotatedVertically=!rotatedVertically;
//        Log.d(TAG, "ship  rotated"+ rotatedVertically);
        InitiateShip();
    }
    public void Place(int[]cellPosition,boolean isCursorInUpperHalf){
//        Log.d(TAG, "Placing ship"+length);
        switch (length){
            case 1:
                    rootCell = cellPosition;
                break;
            case 2:
                if(isCursorInUpperHalf)rootCell=cellPosition;
                else rootCell = new int[] {cellPosition[0],cellPosition[1]+1};
                break;
            case 3:
                rootCell = new int[] {cellPosition[0],cellPosition[1]+1};
                break;
            case 4:
                if(isCursorInUpperHalf) rootCell = new int[] {cellPosition[0],cellPosition[1]+1};
                else rootCell = new int[] {cellPosition[0],cellPosition[1]+2};
                break;
        }
//        Log.d(TAG, "Initiating ship : "+rootCell[0]+":"+rootCell[1]);
        rotatedVertically=true;
        InitiateShip();
    }
    public void  Place(int[] rootCellPos){
        rootCell = rootCellPos;
        rotatedVertically=true;
        InitiateShip();
    }
    public void InitiateShip(){
        corpus.clear();
//        Log.d(TAG, "corpus is empty: "+corpus.isEmpty());
        if(rotatedVertically){
            for (int i = 0; i < length; i++) {
//                Log.d(TAG, "Initiate corpus: "+rootCell[0]+":"+(rootCell[1]-i));
                corpus.add(new int[]{rootCell[0],(rootCell[1]-i)});
            } }
        else {
            for (int i = 0; i < length; i++) {
//                Log.d(TAG, "Initiate corpus: " + (rootCell[0] + i) + ":" + rootCell[1]);
                corpus.add(new int[]{(rootCell[0] + i), rootCell[1]});

            } }
        InitiateShadow();
    }
    public void InitiateShadow(){
        shadow.clear();
        if(!corpus.isEmpty()){
            for (int c = 0; c < corpus.size(); c++){
                InitiateCellShadow(c);
            }
        }
    }
    public void InitiateCellShadow(int cellIndex){
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                int[] currentCell = new int[]{corpus.get(cellIndex)[0]+x,corpus.get(cellIndex)[1]+y};
                if(corpus.contains(currentCell));// shadow cant be placed on ship
                else if(shadow.contains(currentCell));// shadow cant be placed on existing shadow
                else{
                    shadow.add(currentCell);//add shadow cell
                }
            }
        }
    }
    public void LogCorpus(){


        for (int[] part: corpus//deleting old version
        ) {
            Log.d(TAG,"corpus: "+part[0]+":"+part[1]);
            //RedactCellElement(true,part[0],part[1]);
        }
    }

    public ArrayList<int[]> getShadow() {
        return shadow;
    }

    public void setShadow(ArrayList<int[]> shadow) {
        this.shadow = shadow;
    }

    public ArrayList<int[]> getCorpus() {
        return corpus;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int[] getRootCell() {
        return rootCell;
    }

    public void setRootCell(int[] rootCell) {
        this.rootCell = rootCell;
    }
}
