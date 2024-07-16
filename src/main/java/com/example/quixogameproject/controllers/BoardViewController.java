package com.example.quixogameproject.controllers;

import com.example.quixogameproject.models.Board;
import com.example.quixogameproject.models.Player;
import com.example.quixogameproject.util.Observer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.nio.Buffer;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BoardViewController implements Initializable, Observer {

    public Board board = Board.getInstance();
    @FXML
    public GridPane boardGrid;
    @FXML
    public Button b10;
    @FXML
    public Button b20;
    @FXML
    public Button b30;
    @FXML
    public Button b40;
    @FXML
    public Button b50;
    @FXML
    public Button b01;
    @FXML
    public Button b02;
    @FXML
    public Button b03;
    @FXML
    public Button b04;
    @FXML
    public Button b05;
    @FXML
    public Button b16;
    @FXML
    public Button b26;
    @FXML
    public Button b36;
    @FXML
    public Button b46;
    @FXML
    public Button b56;
    @FXML
    public Button b61;
    @FXML
    public Button b62;
    @FXML
    public Button b63;
    @FXML
    public Button b64;
    @FXML
    public Button b65;

    @FXML
    public Label labelActivePlayer;

    @FXML
    public Label labelNbrOfMoves;

    @FXML
    public Label labelTimeElapsed;

    public int nbrOfMoves = 0;

    private int hours = 0;
    private int minutes = 0;
    private int seconds = 0;

    public Player playerOne, playerTwo, currentPlayer;

    private ScheduledExecutorService executorService;

    public int currentRowChoice,currentColChoice;

    public void initializeWithData(String symbolOne, String symbolTwo) {
        playerOne = new Player(symbolOne);
        playerTwo = new Player(symbolTwo);
        currentPlayer = playerOne;
        board.addObserver(this);
        initializeBoard();
    }

    public void initializeBoard(){
        for (int row = 1; row < 6; row++) {
            for (int col = 1; col < 6; col++) {
                if (isLabel(row, col)) {
                    Label l = (Label) boardGrid.getChildren().get(7 * row + col);
                    l.textProperty().bind(board.getCaseValueProperty(row, col));
                } else {
                    HBox hBox = (HBox) boardGrid.getChildren().get(7 * row + col);
                    Button button = (Button) hBox.getChildren().getFirst();
                    button.setOnAction(linkChoixButtonToMoveButton(row, col));
                    Label l = (Label) hBox.getChildren().getLast();
                    l.textProperty().bind(board.getCaseValueProperty(row, col));
                }
            }
        }
        hideMoveButtons();
        linkMoveButtonsToAction();
        labelActivePlayer.setText("C'est au tour du joueur 1, votre symbole : "+playerOne.getSymbole());
        labelNbrOfMoves.setText("Nombre de coups : "+String.valueOf(nbrOfMoves));
        labelTimeElapsed.setText("00:00:00");
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::incrementTime, 0, 1, TimeUnit.SECONDS);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public boolean isLabel(int row, int col) {
        if (boardGrid.getChildren().get(7 * row + col) instanceof Label) {
            return true;
        } else if (boardGrid.getChildren().get(7 * row + col) instanceof HBox) {
            return false;
        }
        return false;
    }

    public boolean isButton(int row, int col) {
        return boardGrid.getChildren().get(7 * row + col) instanceof Button;
    }


    public void hideMoveButtons() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (isButton(i, j)) {
                    Button button = (Button) boardGrid.getChildren().get(7 * i + j);
                    button.setVisible(false);
                }
            }
        }
    }

    public void linkMoveButtonsToAction(){
        System.out.println(currentPlayer.getSymbole());
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (isButton(i, j)) {
                    Button button = (Button) boardGrid.getChildren().get(7 * i + j);
                    if(i == 0){
                        button.setOnAction(actionPushDown(j-1,currentPlayer.getSymbole()));
                    } else if (i == 6) {
                        button.setOnAction(actionPushUp(j-1,currentPlayer.getSymbole()));
                    } else if (j == 0) {
                        button.setOnAction(actionPushRight(i-1,currentPlayer.getSymbole()));
                    } else if (j == 6) {
                        button.setOnAction(actionPushLeft(i-1,currentPlayer.getSymbole()));
                    }
                }
            }
        }
    }

    public void switchPlayer() {
        nbrOfMoves++;
        labelNbrOfMoves.setText("Nombre de coups : "+String.valueOf(nbrOfMoves));
        if(!board.isWinner()){
            if(currentPlayer.equals(playerOne))
            {
                currentPlayer=playerTwo;
                linkMoveButtonsToAction();
                showPossibilitiesForCurrentPlayer();
                labelActivePlayer.setText("C'est au tour du joueur 2, votre symbole : "+playerTwo.getSymbole());
            }
            else{
                currentPlayer=playerOne;
                linkMoveButtonsToAction();
                showPossibilitiesForCurrentPlayer();
                labelActivePlayer.setText("C'est au tour du joueur 1, votre symbole : "+playerOne.getSymbole());
            }
        }else{
            //Crée un nouveau ecran pour gagnant
            System.out.println("cogratulations player :" +currentPlayer.getSymbole()+" won!");
            labelTimeElapsed.setText(labelTimeElapsed.getText());
            executorService.shutdown();
            loadWinnerView();
            hideMoveButtons();
        }
    }

    public void loadWinnerView(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quixogameproject/winner-view.fxml"));
        Parent root = null;
        try {
            root = loader.load();
            Stage winnerStage = new Stage();
            winnerStage.initModality(Modality.APPLICATION_MODAL);
            WinnerViewController winnerViewController  = loader.getController();
            winnerViewController.initializeWithData(currentPlayer.equals(playerOne)?"1":"2",nbrOfMoves,hours,minutes,seconds);
            winnerStage.setTitle("WINNER!");
            winnerStage.setScene(new Scene(root,400,300));
            winnerStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public EventHandler<ActionEvent> actionPushUp(int col, String value) {
        return event -> {
            board.pushCaseUp(currentRowChoice,col, value);
        };
    }

    public EventHandler<ActionEvent> actionPushDown(int col, String value) {
        return event -> {
            board.pushCaseDown(currentRowChoice,col, value);
        };
    }

    public EventHandler<ActionEvent> actionPushRight(int row, String value) {
        return event -> {
            board.pushCaseRight(row,currentColChoice, value);
        };
    }

    public EventHandler<ActionEvent> actionPushLeft(int row, String value) {
        return event -> {
            board.pushCaseLeft(row,currentColChoice,value);
        };
    }

    public void showPossibilitiesForCurrentPlayer() {
        System.out.println("showing possibilities for player "+currentPlayer.getSymbole());
        for (int row = 1; row < 6; row++) {
            for (int col = 1; col < 6; col++) {
                if (boardGrid.getChildren().get(7 * row + col) instanceof HBox) {
                    HBox hBox = (HBox) boardGrid.getChildren().get(7 * row + col);
                    Button button = (Button) hBox.getChildren().getFirst();
                    Label l = (Label) hBox.getChildren().getLast();
                    if(l.getText().equals(currentPlayer.getSymbole()) || l.getText().equals("") ){
                        button.setVisible(true);
                    } else {
                        button.setVisible(false);
                    }
                }
            }
        }
    }



    public EventHandler<ActionEvent> linkChoixButtonToMoveButton(int row, int col) {
        EventHandler<ActionEvent> eventHandler = event -> {
            if (row == 1 && col == 1) {
                b61.setVisible(true);
                b16.setVisible(true);
                currentRowChoice=0;
                currentColChoice=0;
            } else if (row == 1 && col == 5) {
                b65.setVisible(true);
                b10.setVisible(true);
                currentRowChoice=0;
                currentColChoice=4;
            } else if (row == 5 && col == 1) {
                b56.setVisible(true);
                b01.setVisible(true);
                currentRowChoice=4;
                currentColChoice=0;
            } else if (row == 5 && col == 5) {
                b50.setVisible(true);
                b05.setVisible(true);
                currentRowChoice=4;
                currentColChoice=4;
            } else if (row == 1) {
                b16.setVisible(true);
                b10.setVisible(true);
                Button button = (Button) boardGrid.getChildren().get(7 * 6 + col);
                button.setVisible(true);
                currentRowChoice=0;
                currentColChoice=col-1;
            } else if (row == 5) {
                b50.setVisible(true);
                b56.setVisible(true);
                Button button = (Button) boardGrid.getChildren().get(col);
                button.setVisible(true);
                currentRowChoice=4;
                currentColChoice=col-1;
            } else if (col == 1) {
                b61.setVisible(true);
                b01.setVisible(true);
                Button button = (Button) boardGrid.getChildren().get(7 * row + 6 );
                button.setVisible(true);
                currentRowChoice=row-1;
                currentColChoice=0;
            } else if (col == 5) {
                b65.setVisible(true);
                b05.setVisible(true);
                Button button = (Button) boardGrid.getChildren().get(7 * row);
                button.setVisible(true);
                currentRowChoice=row-1;
                currentColChoice=4;
            }
        };

        return eventHandler;
    }

    private void incrementTime() {
        seconds++;
        if (seconds == 60) {
            seconds = 0;
            minutes++;
            if (minutes == 60) {
                minutes = 0;
                hours++;
                if (hours == 24) {
                    // Vous pouvez gérer ici la logique de limitation de la durée maximale
                    // du chronomètre si nécessaire.
                    hours = 0;
                }
            }
        }
        Platform.runLater(() -> updateLabel());
    }

    private void updateLabel() {
        String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        labelTimeElapsed.setText(time);
    }

    private void stopChronometer() {
        executorService.shutdown();
    }

    @Override
    public void update() {
        switchPlayer();
        hideMoveButtons();
    }
}
