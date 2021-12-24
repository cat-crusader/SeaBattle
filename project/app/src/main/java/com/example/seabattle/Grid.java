package com.example.seabattle;

import android.util.Log;

import java.util.Arrays;

public class Grid {// TODO: 24.12.2021 !Currently work only with square size arrays only!
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
    public Grid(boolean[][] _grid){

        grid = _grid.clone();
//        LogGrid();
        sizeX=_grid.length;
        sizeY=_grid.length;
    }
    public void fill(boolean filler){
        for (boolean[] row: grid)
            Arrays.fill(row, filler);
    }
    public boolean insideGrid(int posX, int posY){
        if(posX>sizeX||posY>sizeY)return false;
        if(posX<0||posY<0)return false;//out of grid
        return true;
    }
    public boolean insideGrid(int[]cellCoordinates){
        if(cellCoordinates[0]>sizeX||cellCoordinates[1]>sizeY)return false;
        if(cellCoordinates[0]<0||cellCoordinates[1]<0)return false;//out of grid
        return true;
    }

    public void setCell(int xPos, int yPos, boolean value){
        grid[xPos][yPos] = value;
    }
    public void setCell(int[]cellCoordinates, boolean value){ grid[cellCoordinates[0]][cellCoordinates[1]] = value; }

    public boolean getCell(int posX, int posY){
        if(insideGrid(posX,posY)){
            return grid[posX][posY];
        }
        return false;
    }
    public boolean getCell(int[] cellCoordinates){
        if(insideGrid(cellCoordinates)){
            return grid[cellCoordinates[0]][cellCoordinates[1]];
        }
        return false;
    }

    public Grid AND(Grid _grid){
        Grid result = new Grid(10,10);
        result.fill(false);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if(getCell(i,j)==_grid.getCell(i,j)&& getCell(i,j)==true)result.setCell(i,j,true);
            }
        }
        return result;
    }

    public void logGrid(){
        for (int i = 0; i < sizeX ; i++) {
            String row="";
            for (int j = 0; j < sizeY; j++) {
                row+=grid[j][i] ? 1 +" ": 0 +" ";
            }
            Log.d(TAG, "row"+i+": "+row);
        }
    }
}
