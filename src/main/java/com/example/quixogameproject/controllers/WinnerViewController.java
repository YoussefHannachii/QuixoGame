package com.example.quixogameproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class WinnerViewController implements Initializable {


    @FXML
    Text labelForTheWinner;

    @FXML
    Text labelNbrDeCoups;

    @FXML
    Text labelTempsPartie;

    public void initializeWithData(String winner , int nbrOfMoves , int hours , int minutes, int seconds){
        labelForTheWinner.setText("Félicitations joueur "+ winner +" :)");
        labelNbrDeCoups.setText("Nombre de coup joués :"+nbrOfMoves);
        String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        labelTempsPartie.setText("Temps de la partie :"+time);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
