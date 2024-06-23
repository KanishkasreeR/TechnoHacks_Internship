package application;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeGame extends Application {

    private static final int TILE_SIZE = 20;
    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;

    private int foodX, foodY;
    private List<Position> snake;
    private Direction direction = Direction.RIGHT;
    private boolean gameOver = false;
    private boolean gamePaused = false;

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        snake = new ArrayList<>();
        snake.add(new Position(WIDTH / 2, HEIGHT / 2)); 

        generateFood(); 

        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 100_000_000) { 
                    lastUpdate = now;
                    if (!gameOver && !gamePaused) {
                        update();
                        render(gc);
                    }
                }
            }
        };
        timer.start();

        StackPane root = new StackPane();
        root.getChildren().add(canvas);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP && direction != Direction.DOWN) {
                direction = Direction.UP;
            } else if (event.getCode() == KeyCode.DOWN && direction != Direction.UP) {
                direction = Direction.DOWN;
            } else if (event.getCode() == KeyCode.LEFT && direction != Direction.RIGHT) {
                direction = Direction.LEFT;
            } else if (event.getCode() == KeyCode.RIGHT && direction != Direction.LEFT) {
                direction = Direction.RIGHT;
            } else if (event.getCode() == KeyCode.P) {
                gamePaused = !gamePaused;
            }
        });

        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void update() {
        Position head = snake.get(0);
        int newX = head.getX();
        int newY = head.getY();

        switch (direction) {
            case UP:
                newY--;
                break;
            case DOWN:
                newY++;
                break;
            case LEFT:
                newX--;
                break;
            case RIGHT:
                newX++;
                break;
        }

        if (newX < 0 || newX >= WIDTH || newY < 0 || newY >= HEIGHT || checkSelfCollision(newX, newY)) {
            gameOver = true;
            showGameOverAlert();
            return;
        }

        if (newX == foodX && newY == foodY) {
            snake.add(0, new Position(newX, newY));
            generateFood();
        } else {
            snake.add(0, new Position(newX, newY));
            snake.remove(snake.size() - 1);
        }
    }

    private boolean checkSelfCollision(int x, int y) {
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(i).getX() == x && snake.get(i).getY() == y) {
                return true;
            }
        }
        return false;
    }

    private void generateFood() {
        Random random = new Random();
        do {
            foodX = random.nextInt(WIDTH);
            foodY = random.nextInt(HEIGHT);
        } while (checkFoodCollision());
    }

    private boolean checkFoodCollision() {
        for (Position segment : snake) {
            if (segment.getX() == foodX && segment.getY() == foodY) {
                return true;
            }
        }
        return false;
    }

    private void render(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);

        gc.setFill(Color.GREEN);
        for (Position segment : snake) {
            gc.fillRect(segment.getX() * TILE_SIZE, segment.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        gc.setFill(Color.RED);
        gc.fillOval(foodX * TILE_SIZE, foodY * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    private void showGameOverAlert() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over!");
        alert.setHeaderText(null);
        alert.setContentText("Game Over! Your score: " + (snake.size() - 1));
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private static class Position {
        private final int x;
        private final int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
