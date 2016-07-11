/*
 * Author: Clifton Taylor
 * SID: 800882831
 * Date: 7/9/2016
 * Purpose: This program uses a hill-climbing algorithm to find a valid configuration of 8 queens on an 8 by 8 board.
 */

import java.util.*;

public class EightQueens {
    
    final private int [][] map = new int[8][8];
    final private int [][] testMap = new int[8][8];
    private int heuristic = 0;
    private int queenLocs = 0;
    private int restarts = 0;
    private int moves = 0;
    private boolean newMap = true;
    private int neighbors = 0;
    
    
    public EightQueens( ){ //initializes the map
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                map[i][j] = 0;
            }
        }
    }
    
    
    public void randomizeMap( ){ //randomizes the map
      Random rand = new Random( );
      int num;
        
      while(queenLocs < 8){
            for(int i = 0; i < 8; i++){
                map[rand.nextInt(7)][i] = 1;
                queenLocs++;
                }
            }
      heuristic = heuristic(map);
      if(newMap == false){
        System.out.println("RESTART");
      }
    }
    
    //***************************Heuristic****************************//
    
    public boolean findRowEx(int [][] test, int a){ //determines row conflicts
        boolean exFound = false;
        int count = 0;
        
        for(int i = 0; i < 8; i++){
            if(test[i][a] == 1){
                count++;
            }
        }
        if(count > 1){
            exFound = true;
        }
        return exFound;
    }
    public boolean findColEx(int [][] test, int j){ //determines column conflicts
        boolean exFound = false;
        int count = 0;
        for(int i = 0; i < 8; i++){
            if(test[j][i] == 1){
                count++;
            }
        }
        if(count > 1){
          exFound = true;
        }
        return exFound;
    }
    
    public boolean findDiaEx(int [][] test, int a, int b){//determines diagonal conflicts
        boolean diaFound = false;
        
        for(int i = 1; i < 8; i++){
            if(diaFound){
                break;
            }

            if((a+i < 8)&&(b+i < 8)){
                if(test[a+i][b+i] == 1){
                    diaFound = true;
                }
            }
            if((a-i >= 0)&&(b-i >= 0)){
                if(test[a-i][b-i] == 1){
                    diaFound = true;
                }
            }
            if((a+i < 8)&&(b-i >= 0)){
                if(test[a+i][b-i] == 1){
                    diaFound = true;
                }
            }    
            if((a-i >= 0)&&(b+i < 8)){
                if(test[a-i][b+i] == 1){
                    diaFound = true;
                }
            }    
        }
        return diaFound;
    }
    
    public int heuristic(int [][] test){//Counts the number of queens in conflict
    int count = 0;
    boolean rowEx;
    boolean colEx;
    boolean diaEx;
        
        for(int i = 0; i < 8; i++){
            for(int j= 0; j < 8; j++){
                if(test[i][j] == 1){
                    rowEx = findRowEx(test, j);
                    colEx = findColEx(test, i);
                    diaEx = findDiaEx(test, i, j);
                    
                    if(rowEx || colEx || diaEx){
                        count++;
                    }
                }
            }
        }
        return count;
    }
    //***********************Move Queen***********************//
    public void moveQueen( ){ //moves a queen and determines whether to continue to a new state or restart or to summarize solution
        int[][] hArray = new int[8][8];
        int colCount;
        int minCol;
        int minRow;
        int prevColQueen = 0;
        boolean noSolution = true;
        newMap = false;
        
        while(noSolution){
            colCount = 0;
        
            for(int i = 0; i < 8; i++){
                System.arraycopy(map[i], 0, testMap[i], 0, 8);
            }
            while(colCount < 8){
                for(int i = 0; i < 8;i++){
                    testMap[i][colCount] = 0;
                }
                for(int i = 0; i < 8; i++){
                    if(map[i][colCount] == 1){
                        prevColQueen = i;
                    }
                    testMap[i][colCount] = 1;
                    hArray[i][colCount] = heuristic(testMap);
                    testMap[i][colCount] = 0;
                }
                testMap[prevColQueen][colCount] = 1;
                colCount++;
            }
            
            if(determineRestart(hArray)){
                queenLocs = 0;
                for(int i = 0; i < 8; i++){
                    for(int j = 0; j < 8; j++){
                        map[i][j] = 0;
                    }
                }
                randomizeMap( );
                restarts++;
            }
        
            minCol = findMinCol(hArray);
            minRow = findMinRow(hArray);
        
            for(int i = 0; i < 8; i++){
                map[i][minCol] = 0;
            }
        
            map[minRow][minCol] = 1;
            moves++;
            heuristic = heuristic(map);
            
            if(heuristic(map) == 0){
                System.out.println("\nCurrent State");
                for(int i = 0; i < 8; i++){
                    for(int j = 0; j < 8; j++){
                        System.out.print(map[i][j] + " ");
                    }
                    System.out.print("\n");
                }
            System.out.println("Solution Found!");
            System.out.println("State changes: " + moves);
            System.out.println("Restarts: " + restarts);
            break;
            }
 
            System.out.println("\n");
            System.out.println("Current h: " + heuristic);
            System.out.println("Current State");
            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++){
                    System.out.print(map[i][j] + " ");
                }
                System.out.print("\n");
            }
            System.out.println("Neighbors found with lower h: " + neighbors);
            System.out.println("Setting new current State");
        }
    }
    public int findMinCol(int[][] test){ //finds column of minimum neighbor state
        int minCol = 8;
        int minVal = 8;
        int count = 0;
        
        for(int i = 0; i < 8; i++){
          for(int j = 0; j < 8; j++){
              if(test[i][j] < minVal){
                  minVal = test[i][j];
                  minCol = j;
              }
              if(test[i][j] < heuristic){
                  count++;
              }
          }
        }
        neighbors = count;
        return minCol;
    }
    public int findMinRow(int[][] test){ //finds row of minimum neighbor state
        int minRow = 8;
        int minVal = 8;
        
        for(int i = 0; i < 8; i++){
          for(int j = 0; j < 8; j++){
              if(test[i][j] < minVal){
                  minVal = test[i][j];
                  minRow = i;
              }
          }
        }
        return minRow;
    }
    public boolean determineRestart(int [][] test){// determines whether restart is necessary
        int minVal = 8;
        boolean restart = false;
        
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(test[i][j] < minVal){
                    minVal = test[i][j];
                }
            }
        }
        if(neighbors == 0){
            restart = true;
        }
        return restart;
    }
    /************************* Main **********************/
    public static void main(String[] args) {// creates object, creates initial random map, then initiates state change
     EightQueens one = new EightQueens( );
     one.randomizeMap();
     one.moveQueen();
    }
}

    

