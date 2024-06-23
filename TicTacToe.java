package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TicTacToe extends Application {

    private static final int BOARD_SIZE = 3;
    private Button[][] buttons = new Button[BOARD_SIZE][BOARD_SIZE];
    private boolean playerXTurn = true; 
    private int movesMade = 0;

    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Button button = new Button();
                button.setMinSize(100, 100); 
                button.setStyle("-fx-font-size: 24;");
                buttons[i][j] = button;
                gridPane.add(button, j, i); 
                int finalI = i;
                int finalJ = j;
                button.setOnAction(e -> handleButtonClick(finalI, finalJ));
            }
        }

        Scene scene = new Scene(gridPane, 320, 320);
        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleButtonClick(int i, int j) {
        Button button = buttons[i][j];
        if (button.getText().isEmpty()) { 
            if (playerXTurn) {
                button.setText("X");
            } else {
                button.setText("O");
            }
            movesMade++;
            if (checkForWin(i, j)) {
                showWinAlert(playerXTurn ? "X" : "O");
                resetGame();
            } else if (movesMade == BOARD_SIZE * BOARD_SIZE) {
                showDrawAlert();
                resetGame();
            } else {
                playerXTurn = !playerXTurn; 
            }
        }
    }

    private boolean checkForWin(int row, int col) {
        String symbol = playerXTurn ? "X" : "O";

        // Check row
        boolean win = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (!buttons[row][i].getText().equals(symbol)) {
                win = false;
                break;
            }
        }
        if (win) return true;

        // Check column
        win = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (!buttons[i][col].getText().equals(symbol)) {
                win = false;
                break;
            }
        }
        if (win) return true;

        if (row == col) { 
            win = true;
            for (int i = 0; i < BOARD_SIZE; i++) {
                if (!buttons[i][i].getText().equals(symbol)) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }
        if (row + col == BOARD_SIZE - 1) { 
            win = true;
            for (int i = 0; i < BOARD_SIZE; i++) {
                if (!buttons[i][BOARD_SIZE - 1 - i].getText().equals(symbol)) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }

        return false;
    }

    private void showWinAlert(String winner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("Player " + winner + " wins!");
        alert.show();
    }

    private void showDrawAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("It's a draw!");
        alert.show();
    }

    private void resetGame() {
        playerXTurn = true;
        movesMade = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                buttons[i][j].setText("");
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
