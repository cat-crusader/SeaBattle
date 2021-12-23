package com.example.seabattle;

import android.view.GestureDetector;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Fleet {

    private Grid shipsGrid;
    private Grid shadowGrid;

    private int[] shipsAmmount ={4,3,2,1};
    private int shipsCount=0;
    private Stack<Ship> shipsStack = new Stack<>();

    public Fleet(){
        shipsGrid = new Grid(10,10);
        shadowGrid = new Grid(10,10);
        shadowGrid.fill(true);
        shipsGrid.fill(false);
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
    }//Check if ship can be placed on *this* grid
    boolean InBounds(int[] cell){

        if(cell[0]>9||cell[1]>9)return false;
        if(cell[0]<0||cell[1]<0)return false;//out of grid

        return true;
    }//Check if cell coords is inside grid
    public void AutoPlaceFleet(){
        for (int t = 0; t < shipsAmmount.length; t++) {
            int shipsInType = shipsAmmount[t];
            for (int i = 0; i < shipsInType; i++) {
                AutoPlaceShip(t+1);
            }


        }
    }//auto place all ships on grid
    public void AutoPlaceShip(int shipLength){
        int shipAmmount = shipsStack.size();
        while (shipAmmount==shipsStack.size()&&shipsAmmount[shipLength-1]>0){
            Random r = new Random();
            boolean rotation = r.nextBoolean();
            int randomYPos = (shipLength-1) + (int) (Math.random() * 10);
            int randomXPos = 0 + (int) (Math.random() * (11-shipLength));
//            PlaceShip(new int[]{randomXPos,randomYPos},shipLength);
        }



    }//auto place type of ship on grid

}
