package com.example.seabattle;

import android.util.Log;

import java.util.Arrays;

public class Grid {
    private  static final String TAG = "GRID";

    private boolean[][] grid;

    private int sizeY;
    private int sizeX;

    public boolean[][] getGrid() {
        return grid;
    }

    public void setGrid(boolean[][] grid) {
        this.grid = grid;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public Grid(int _sizeX, int _sizeY){
        sizeX = _sizeX;
        sizeY = _sizeY;
        grid = new boolean[sizeX][sizeY];
    }
    public void fill(boolean filler){
        for (boolean[] row: grid)
            Arrays.fill(row, filler);
    }

    public void SetCell(int xPos,int yPos,boolean value){
        grid[xPos][yPos] = value;
    }
    public void LogGrid(){
        for (int i = 0; i < sizeX ; i++) {
            String row="";
            for (int j = 0; j < sizeY; j++) {
                row+=grid[j][i] ? 1 +" ": 0 +" ";
            }
            Log.d(TAG, "row"+i+": "+row);
        }
    }
}
