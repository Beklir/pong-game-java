package org.example;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;

public class Controller {
    private Stage primaryStage;
    private Scene scene;
    private Text scoreboard;
    private Circle ball;
    private Rectangle player1;
    private Rectangle player2;

    Controller(Stage primaryStage,Scene scene, Text scoreboard, Circle ball, Rectangle player1, Rectangle player2) {
        this.primaryStage = primaryStage;
        this.scene = scene;
        this.scoreboard = scoreboard;
        this.ball = ball;
        this.player1 = player1;
        this.player2 = player2;
    }
    int player1Score = 0;
    int player2Score = 0;

    double ballXSpeed = 2;
    double ballYSpeed = 2;

    double paddleSpeed = 5;
    Set<KeyCode> pressedKeys = new HashSet<>();

    public void play() {
        scene.setOnKeyPressed(e -> pressedKeys.add(e.getCode()));
        scene.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                ball.setCenterX(ball.getCenterX() + ballXSpeed);
                ball.setCenterY(ball.getCenterY() + ballYSpeed);

                if (ball.getCenterY() + ballYSpeed > scene.getHeight()) {
                    ballYSpeed = -Math.abs(ballYSpeed);
                }

                if (ball.getCenterY() + ballYSpeed < 0) {
                    ballYSpeed = Math.abs(ballYSpeed);
                }
                if (ball.getCenterX() + ballXSpeed > scene.getWidth()) {
                    player1Score++;
                    scoreboard.setText(player1Score + " : " + player2Score);

                    ball.setCenterX(scene.getWidth() / 2);
                    ball.setCenterY(scene.getHeight() / 2);
                    ballXSpeed = -2;
                    ballYSpeed = 2;
                }
                if (ball.getCenterX() + ballXSpeed < 0) {
                    player2Score++;
                    scoreboard.setText(player1Score + " : " + player2Score);

                    ball.setCenterX(scene.getWidth() / 2);
                    ball.setCenterY(scene.getHeight() / 2);
                    ballXSpeed = 2;
                    ballYSpeed = 2;
                }


                if (ball.getBoundsInParent().intersects(player1.getBoundsInParent())) {
                    ballXSpeed = Math.abs(ballXSpeed);
                    ballXSpeed *= 1.05;
                    ballYSpeed *= 1.05;
                }
                if (ball.getBoundsInParent().intersects(player2.getBoundsInParent())) {
                    ballXSpeed = -Math.abs(ballXSpeed);
                    ballXSpeed *= 1.05;
                    ballYSpeed *= 1.05;
                }


                if (pressedKeys.contains(KeyCode.W) && player1.getY() - paddleSpeed > 0) {
                    player1.setY(player1.getY() - paddleSpeed);
                }
                if (pressedKeys.contains(KeyCode.S) && player1.getY() + paddleSpeed < scene.getHeight() - player1.getHeight()) {
                    player1.setY(player1.getY() + paddleSpeed);
                }
                if (pressedKeys.contains(KeyCode.I) && player2.getY() - paddleSpeed > 0) {
                    player2.setY(player2.getY() - paddleSpeed);
                }
                if (pressedKeys.contains(KeyCode.K) && player2.getY() + paddleSpeed < scene.getHeight() - player2.getHeight()) {
                    player2.setY(player2.getY() + paddleSpeed);
                }

                if (pressedKeys.contains(KeyCode.R)) {
                    player1Score = 0;
                    player2Score = 0;
                    scoreboard.setText(player1Score + " : " + player2Score);

                    ball.setCenterX(scene.getWidth() / 2);
                    ball.setCenterY(scene.getHeight() / 2);
                    ballXSpeed = 2;
                    ballYSpeed = 2;
                }
            }
        };
        timer.start();

    }
}
