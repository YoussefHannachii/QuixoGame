package com.example.quixogameproject.models;

import com.example.quixogameproject.util.Observer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private static Board instance;
    private final StringProperty[][] board = new StringProperty[5][5];
    private List<Observer> observers = new ArrayList<>();


    private Board() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                board[row][col] = new SimpleStringProperty("");
            }
        }
    }

    public static Board getInstance() {
        // S'assurer qu'il n'y a qu'une seule instance de la classe
        if (instance == null) {
            instance = new Board();
        }
        return instance;
    }
    public static boolean checkCoin(int row,int col,int cols,int rows){
        if(row==0 && col==0){
            return true;
        } else if (row==0 && col==cols) {
            return true;
        } else if (row==rows && col==0) {
            return true;
        } else if (row==rows && col==cols) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkExtremite(int row,int col,int cols,int rows){
        if(row==0 ){
            return true;
        } else if (col==cols) {
            return true;
        } else if (col==0) {
            return true;
        } else if (row==rows) {
            return true;
        } else {
            return false;
        }
    }

    public StringProperty[][] getBoard() {
        return board;
    }

    public StringProperty getCaseValueProperty(int row, int col){
        return board[row-1][col-1];
    }

    public String getCaseValue(int row, int col) {
        return board[row][col].get();
    }

    public void setCaseValue(int row, int col, String value) {
        board[row][col].set(value);
    }

    public void pushCaseUp(int row,int col, String value){
        setCaseValue(row,col,"");
        for(int i=0;i<4;i++){
            setCaseValue(i,col,getCaseValue(i+1,col));
        }
        setCaseValue(4,col,value);
        notifyObservers();
    }

    public void pushCaseDown(int row,int col, String value){
        setCaseValue(row,col,"");
        for(int i=4;i>0;i--){
            setCaseValue(i,col,getCaseValue(i-1,col));
        }
        setCaseValue(0,col,value);
        notifyObservers();
    }

    public void pushCaseRight(int row,int col, String value){
        setCaseValue(row,col,"");
        for(int i=4;i>0;i--){
            setCaseValue(row,i,getCaseValue(row,i-1));
        }
        setCaseValue(row,0,value);
        notifyObservers();
    }

    public void pushCaseLeft(int row,int col,String value){
        setCaseValue(row,col,"");
        for(int i=0;i<4;i++){
            setCaseValue(row,i,getCaseValue(row,i+1));
        }
        setCaseValue(row,4,value);
        notifyObservers();
    }

    public boolean isWinner(){
        for (int i = 0; i < 5; i++) {
            if (checkSequence(getCaseValue(i,0),getCaseValue(i,1),getCaseValue(i,2), getCaseValue(i,3), getCaseValue(i,4))) {
                return true;  // Suite dans une ligne
            }
        }
        for (int j = 0; j < 5; j++) {
            if (checkSequence(getCaseValue(0,j),getCaseValue(1,j),getCaseValue(2,j), getCaseValue(3,j), getCaseValue(4,j))) {
                return true;  // Suite dans une ligne
            }
        }
        if (checkSequence(getCaseValue(0,0), getCaseValue(1,1), getCaseValue(2,2), getCaseValue(3,3), getCaseValue(4,4)) ||
                checkSequence(getCaseValue(0,4), getCaseValue(1,3), getCaseValue(2,2), getCaseValue(3,1), getCaseValue(4,0))) {
            return true;  // Suite dans une diagonale
        }
        return false;
    }
    private boolean checkSequence(String symbol1,String symbol2,String symbol3,String symbol4, String symbol5) {

        return symbol1.equals(symbol2) && symbol2.equals(symbol3) && symbol3.equals(symbol4) && symbol4.equals(symbol5) && !symbol1.isEmpty();
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
