package com.example.quixogameproject.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameViewController implements Initializable {

    private String choixUtilisateurUn,choixUtilisateurDeux;
    @FXML
    private Label secondLabelToShow;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private Button deuxiemeButtonToShow;
    @FXML
    private Label firstLabelToShow;
    @FXML
    private HBox choixSymbole;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hideSecondScreen();
    }

    private void hideSecondScreen(){
        secondLabelToShow.setVisible(false);
        deuxiemeButtonToShow.setVisible(false);
    }

    private void hideFirstScreenAndShowSecond(){
        firstLabelToShow.setVisible(false);
        choixSymbole.setVisible(false);
        secondLabelToShow.setVisible(true);
        deuxiemeButtonToShow.setVisible(true);
    }

    public void getSelectedRadio(){
        choixUtilisateurUn = (String) toggleGroup.getSelectedToggle().getUserData();
        if(choixUtilisateurUn.equals("X")){
            choixUtilisateurDeux="O";
        }else{
            choixUtilisateurDeux="X";
        }
        secondLabelToShow.setText("Joueur 2 votre symbole est : " + choixUtilisateurDeux) ;
        hideFirstScreenAndShowSecond();
    }

    public void commencerPartie() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quixogameproject/board-view.fxml"));
        Parent root = loader.load();
        BoardViewController boardViewController = loader.getController();
        boardViewController.initializeWithData(choixUtilisateurUn,choixUtilisateurDeux);
        Stage boardStage = new Stage();
        boardStage.initModality(Modality.APPLICATION_MODAL);
        boardStage.setTitle("One V One");
        boardStage.setScene(new Scene(root,800,600));
        boardStage.show();

        Scene scene = deuxiemeButtonToShow.getScene();

        // Obtenez la fenêtre parente à partir de la scène
        Stage gameStage = (Stage) scene.getWindow();

        // Fermez la fenêtre parente après avoir ouvert la nouvelle fenêtre
        gameStage.close();
    }
}
