package com.example.quixogameproject.controllers;

import com.example.quixogameproject.QuixoApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuViewController {
    public void handleClickOneVOne() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quixogameproject/game-view.fxml"));
        Parent root = loader.load();
        Stage gameStage = new Stage();
        gameStage.initModality(Modality.APPLICATION_MODAL);
        gameStage.setTitle("One V One");
        gameStage.setScene(new Scene(root,800,600));
        gameStage.show();
    }

    public void click(){
        System.out.println("click!");
    }
}
