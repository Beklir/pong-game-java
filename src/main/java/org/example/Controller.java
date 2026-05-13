package org.example;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.HashSet;
import java.util.Set;

public class Controller {
    private Scene scene;
    private Text scoreboard;
    private Circle ball;
    private Rectangle player1;
    private Rectangle player2;

    private static final double BALL_BASE_SPEED = 120.0;
    private static final double PADDLE_SPEED = 300.0;
    private static final double MAX_BALL_SPEED = 600.0;
    private static final double SPEED_INCREMENT = 1.05;

    Controller(Scene scene, Text scoreboard, Circle ball, Rectangle player1, Rectangle player2) {
        this.scene = scene;
        this.scoreboard = scoreboard;
        this.ball = ball;
        this.player1 = player1;
        this.player2 = player2;
    }

    int player1Score = 0;
    int player2Score = 0;

    double ballXSpeed = BALL_BASE_SPEED;
    double ballYSpeed = BALL_BASE_SPEED;

    Set<KeyCode> pressedKeys = new HashSet<>();

    private Long lastFrameTime = null;
    private boolean rWasPressed = false;

    public void play() {
        scene.setOnKeyPressed(e -> pressedKeys.add(e.getCode()));
        scene.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));
        scene.setOnMouseClicked(e -> scene.getRoot().requestFocus());

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastFrameTime == null) {
                    lastFrameTime = now;
                    return;
                }
                double elapsedSeconds = (now - lastFrameTime) / 1_000_000_000.0;
                lastFrameTime = now;

                if (elapsedSeconds > 0.05) {
                    elapsedSeconds = 0.05;
                }

                ball.setCenterX(ball.getCenterX() + ballXSpeed * elapsedSeconds);
                ball.setCenterY(ball.getCenterY() + ballYSpeed * elapsedSeconds);

                double ballRadius = ball.getRadius();

                if (ball.getCenterY() - ballRadius < 0) {
                    ball.setCenterY(ballRadius);
                    ballYSpeed = Math.abs(ballYSpeed);
                }

                if (ball.getCenterY() + ballRadius > scene.getHeight()) {
                    ball.setCenterY(scene.getHeight() - ballRadius);
                    ballYSpeed = -Math.abs(ballYSpeed);
                }

                if (ball.getCenterX() + ballRadius > scene.getWidth()) {
                    player1Score++;
                    scoreboard.setText(player1Score + " : " + player2Score);
                    centerBall();
                    ballXSpeed = -BALL_BASE_SPEED;
                    ballYSpeed = BALL_BASE_SPEED;
                }

                if (ball.getCenterX() - ballRadius < 0) {
                    player2Score++;
                    scoreboard.setText(player1Score + " : " + player2Score);
                    centerBall();
                    ballXSpeed = BALL_BASE_SPEED;
                    ballYSpeed = BALL_BASE_SPEED;
                }

                if (ball.getBoundsInParent().intersects(player1.getBoundsInParent())) {
                    ball.setCenterX(player1.getX() + player1.getWidth() + ballRadius);
                    ballXSpeed = Math.abs(ballXSpeed) * SPEED_INCREMENT;
                    ballYSpeed *= SPEED_INCREMENT;
                    clampSpeeds();
                }

                if (ball.getBoundsInParent().intersects(player2.getBoundsInParent())) {
                    ball.setCenterX(player2.getX() - ballRadius);
                    ballXSpeed = -Math.abs(ballXSpeed) * SPEED_INCREMENT;
                    ballYSpeed *= SPEED_INCREMENT;
                    clampSpeeds();
                }

                if (pressedKeys.contains(KeyCode.W)) {
                    player1.setY(Math.max(0, player1.getY() - PADDLE_SPEED * elapsedSeconds));
                }
                if (pressedKeys.contains(KeyCode.S)) {
                    player1.setY(Math.min(scene.getHeight() - player1.getHeight(), player1.getY() + PADDLE_SPEED * elapsedSeconds));
                }
                if (pressedKeys.contains(KeyCode.I)) {
                    player2.setY(Math.max(0, player2.getY() - PADDLE_SPEED * elapsedSeconds));
                }
                if (pressedKeys.contains(KeyCode.K)) {
                    player2.setY(Math.min(scene.getHeight() - player2.getHeight(), player2.getY() + PADDLE_SPEED * elapsedSeconds));
                }

                boolean rPressed = pressedKeys.contains(KeyCode.R);
                if (rPressed && !rWasPressed) {
                    player1Score = 0;
                    player2Score = 0;
                    scoreboard.setText(player1Score + " : " + player2Score);
                    centerBall();
                    ballXSpeed = BALL_BASE_SPEED;
                    ballYSpeed = BALL_BASE_SPEED;
                }
                rWasPressed = rPressed;
            }
        };
        timer.start();
    }

    private void centerBall() {
        ball.setCenterX(scene.getWidth() / 2);
        ball.setCenterY(scene.getHeight() / 2);
    }

    private void clampSpeeds() {
        double currentSpeed = Math.sqrt(ballXSpeed * ballXSpeed + ballYSpeed * ballYSpeed);
        if (currentSpeed > MAX_BALL_SPEED) {
            double scale = MAX_BALL_SPEED / currentSpeed;
            ballXSpeed *= scale;
            ballYSpeed *= scale;
        }
    }
}
