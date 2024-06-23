package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Random;

public class Main extends Application {

    public static void main(String[] args) {
        System.setProperty("prism.order", "sw");

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Random Password Generator");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(10);
        grid.setHgap(10);

        Label lengthLabel = new Label("Password Length:");
        GridPane.setConstraints(lengthLabel, 0, 0);

        TextField lengthInput = new TextField();
        lengthInput.setPromptText("Enter length");
        GridPane.setConstraints(lengthInput, 1, 0);

        CheckBox includeUppercase = new CheckBox("Include Uppercase Letters");
        GridPane.setConstraints(includeUppercase, 0, 1);

        CheckBox includeLowercase = new CheckBox("Include Lowercase Letters");
        GridPane.setConstraints(includeLowercase, 0, 2);

        CheckBox includeDigits = new CheckBox("Include Digits");
        GridPane.setConstraints(includeDigits, 0, 3);

        CheckBox includeSpecial = new CheckBox("Include Special Characters");
        GridPane.setConstraints(includeSpecial, 0, 4);

        Button generateButton = new Button("Generate Password");
        GridPane.setConstraints(generateButton, 1, 5);

        Label passwordLabel = new Label("Generated Password:");
        GridPane.setConstraints(passwordLabel, 0, 6);

        TextField passwordOutput = new TextField();
        passwordOutput.setEditable(false);
        GridPane.setConstraints(passwordOutput, 1, 6);

        generateButton.setOnAction(e -> {
            try {
                int length = Integer.parseInt(lengthInput.getText());
                boolean upper = includeUppercase.isSelected();
                boolean lower = includeLowercase.isSelected();
                boolean digits = includeDigits.isSelected();
                boolean special = includeSpecial.isSelected();

                String password = generatePassword(length, upper, lower, digits, special);
                passwordOutput.setText(password);
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid number for the length.");
                alert.showAndWait();
            } catch (IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        });

        grid.getChildren().addAll(lengthLabel, lengthInput, includeUppercase, includeLowercase, includeDigits, includeSpecial, generateButton, passwordLabel, passwordOutput);

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String generatePassword(int length, boolean includeUppercase, boolean includeLowercase, boolean includeDigits, boolean includeSpecial) {
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!@#$%^&*()-_=+<>?";

        StringBuilder characterPool = new StringBuilder();

        if (includeUppercase) {
            characterPool.append(uppercase);
        }
        if (includeLowercase) {
            characterPool.append(lowercase);
        }
        if (includeDigits) {
            characterPool.append(digits);
        }
        if (includeSpecial) {
            characterPool.append(special);
        }

        if (characterPool.length() == 0) {
            throw new IllegalArgumentException("At least one character type should be selected.");
        }

        Random random = new Random();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characterPool.length());
            password.append(characterPool.charAt(index));
        }

        return password.toString();
    }
}
