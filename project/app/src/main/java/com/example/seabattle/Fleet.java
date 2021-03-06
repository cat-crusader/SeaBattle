package com.example.seabattle;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Fleet implements Serializable{
    private  static final String TAG = "Fleet";
    private Grid shipsGrid;
    private Grid shadowGrid;
    private Grid hitGrid;

    private boolean finished;

    private int[] shipsAmmount ={4,3,2,1};
    private int shipsCount=0;

    public Stack<Ship> getShipsStack() {
        return shipsStack;
    }
    public void setShipsStack(Stack<Ship> shipsStack) {
        this.shipsStack = shipsStack;
    }

    private  Stack<Ship> shipsStack = new Stack<>();

    public EventManager events;


    public Fleet(){//constructor for placing
        shipsGrid = new Grid(10,10);
        shipsGrid.fill(false);
        shadowGrid = new Grid(10,10);
        shadowGrid.fill(true);
        hitGrid = new Grid(10,10);
        hitGrid.fill(false);


        this.events = new EventManager("placing_update","battle_update");
    }
    public Fleet(Grid _shipsGrid){//constructor for battle
        shipsGrid = _shipsGrid;
        hitGrid = new Grid(10,10);
        hitGrid.fill(false);

        this.events = new EventManager("placing_update","battle_update");
        events.notify("battle_update");
    }

    public Grid getShipsGrid() {
        return shipsGrid;
    }
    public Grid getHitGrid() {
        return hitGrid;
    }

    public void setShipsGrid(Grid shipsGrid) {
        this.shipsGrid = shipsGrid;
    }
    //region === Placing Region ===
    boolean canBePlaced(Ship ship){
        ArrayList<int[]> shipPart = ship.getCorpus();
        for (int[] part: shipPart
        ) {
            if(part[0]>9||part[1]>9)return false;
            if(part[0]<0||part[1]<0)return false;//out of grid

            if(!shadowGrid.getGrid()[part[0]][part[1]])return false;

        }


        return true;
    }//Check if ship can be placed on *this* grid
    boolean inBounds(int[] cell){

        if(cell[0]>9||cell[1]>9)return false;
        if(cell[0]<0||cell[1]<0)return false;//out of grid

        return true;
    }//Check if cell coords is inside grid
    public void autoPlaceFleet(){
        for (int t = shipsAmmount.length-1; t >=0; t--) {
            int shipsInType = shipsAmmount[t];//ship ammount of this type
            for (int i = 0; i < shipsInType; i++) {
                autoPlaceShip(t+1);
            }


        }
    }//auto place all ships on grid
    public void autoPlaceShip(int shipLength){
        int shipAmmount = shipsStack.size();// current ammount of ships
        Random r = new Random();
        boolean rotation = r.nextBoolean();
        while (shipAmmount==shipsStack.size()){

            int randomYPos = (shipLength-1) + (int) (Math.random() * 10);
            int randomXPos = 0 + (int) (Math.random() * (11-shipLength));
            placeShip(new int[]{randomXPos,randomYPos},shipLength);

        }
        if(rotation){
            Log.d(TAG,"Ship should be rotated"+shipsStack.peek().getLength());
            rotateShip();
        }


    }//auto place type of ship on grid

    public void placeShip(int[] cell, int length) {
        Ship newShip = new Ship(length);
        newShip.Place(cell);


        if (!shipsStack.empty()) {//create shadow of previous ship
            ArrayList<int[]> shadowList = shipsStack.peek().getShadow();
            for (int[] part : shadowList
            ) {
//            if(InBounds(part))
                if (inBounds(part)) shadowGrid.setCell(part[0], part[1], false);
                //RedactCellElement(true,part[0],part[1]);
            }
        }

        if (!canBePlaced(newShip)) return;
        if (shipsAmmount[length - 1] <= 0) return;


        shipsStack.add(newShip);
        shipsAmmount[length - 1]--;
        ArrayList<int[]> shipList = shipsStack.peek().getCorpus();
        for (int[] part : shipList
        ) {
            shipsGrid.setCell(part[0], part[1], true);
            //RedactCellElement(true,part[0],part[1]);
        }


    events.notify("placing_update");
    }
    public void rotateShip(){

        Ship rotatedShip = shipsStack.peek();
        rotatedShip.Rotate();

        if(!canBePlaced(rotatedShip))return;
        rotatedShip.Rotate();
        ArrayList<int[]> rotatedShipList = rotatedShip.getCorpus();




        shipsGrid.logGrid();
        for (int[] part: rotatedShipList//deleting old version
        ) {
            shipsGrid.setCell(part[0],part[1],false);
//            Log.d(TAG,"deleting: "+part[0]+":"+part[1]);
            //RedactCellElement(true,part[0],part[1]);
        }
        rotatedShip.Rotate();
        rotatedShipList = rotatedShip.getCorpus();
//        shipsGrid.LogGrid();
        for (int[] part: rotatedShipList//setting rotated version
        ) {
            shipsGrid.setCell(part[0],part[1],true);
            //RedactCellElement(true,part[0],part[1]);
        }
        shipsStack.pop();
        shipsStack.add(rotatedShip);

        events.notify("placing_update");
//        uiTableManager.UpdateTableByValue(shipsGrid,tableView,R.drawable.ship_sprite,R.drawable.empty_cell_sprite);
//        shipsGrid.LogGrid();
    }//try to rotate ship on grid// updates UI table
    //endregion

    //region === Combat Region ===
    public boolean takeHit(int[]cellCoordinates){
        if(!hitGrid.getCell(cellCoordinates)) {
            hitGrid.setCell(cellCoordinates, true);
            return true;
        }
        else {
            return false;
        }
    }
    public boolean takeHit(int posX, int posY){
        if(!hitGrid.getCell(posX,posY)) {
            hitGrid.setCell(posX,posY, true);
            Log.d(TAG,"Got hitted:"+posX+":"+posY);
            events.notify("battle_update");
            return true;
        }
        else {
            return false;
        }
    }
    //endregion
    }
