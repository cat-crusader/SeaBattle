package com.example.seabattle;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class Ship {
    private  static final String TAG = "SHIP";
    private int length;
    private int[]rootCell;
    private ArrayList<int[]> corpus;
    private boolean turnVertically;

    public Ship(int len){
        length=len;
        rootCell=new int[]{-1,-1};
        corpus = new ArrayList<>();
    }
    public void Turn(){
        InitiateShip(!turnVertically);
    }
    public void Place(int[]cellPosition,boolean isCursorInUpperHalf){
        Log.d(TAG, "Placing ship"+length);
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
        Log.d(TAG, "Initiating ship : "+rootCell[0]+":"+rootCell[1]);
        InitiateShip(true);
    }
    public void InitiateShip(boolean vertically){
        turnVertically=vertically;
        corpus.clear();
        if(vertically){
            for (int i = 0; i < length; i++) {
                Log.d(TAG, "Initiate corpus: "+rootCell[0]+":"+(rootCell[1]-i));
                corpus.add(new int[]{rootCell[0],(rootCell[1]-i)});
            } }
        else {
            for (int i = 0; i < length; i++) {
                Log.d(TAG, "Initiate corpus: " + (rootCell[0] + i) + ":" + rootCell[1]);
                corpus.add(new int[]{(rootCell[0] + i), rootCell[1]});

            } }
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
