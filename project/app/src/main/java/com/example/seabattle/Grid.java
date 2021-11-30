package com.example.seabattle;

public class Grid {

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

    public Grid(int sizeX, int sizeY){
        sizeX = sizeX;
        sizeY = sizeY;
        grid = new boolean[sizeX][sizeY];
    }

    public void SetCell(int xPos,int yPos,boolean value){
        grid[xPos][yPos] = value;
    }
}
