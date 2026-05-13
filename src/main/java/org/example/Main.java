package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Text scoreboard = new Text("0 : 0");
        scoreboard.setFill(Color.WHITE);
        scoreboard.setTextAlignment(TextAlignment.CENTER);
        scoreboard.setStyle("-fx-font-family: Arial; -fx-font-size: 20px;");

        Circle ball = new Circle(5);
        ball.setFill(Color.WHITE);

        Rectangle leftPaddle = new Rectangle(5, 100);
        leftPaddle.setFill(Color.WHITE);

        Rectangle rightPaddle = new Rectangle(5, 100);
        rightPaddle.setFill(Color.WHITE);

        Pane root = new Pane();
        root.getChildren().addAll(scoreboard, ball, leftPaddle, rightPaddle);

        Scene scene = new Scene(root, 600, 400);
        scene.setFill(Color.BLACK);

        scoreboard.setWrappingWidth(scene.getWidth());
        scoreboard.setY(20);

        ball.setCenterX(scene.getWidth() / 2);
        ball.setCenterY(scene.getHeight() / 2);

        leftPaddle.setX(5);
        leftPaddle.setY(scene.getHeight() / 2 - 30);

        rightPaddle.setX(scene.getWidth() - 10);
        rightPaddle.setY(scene.getHeight() / 2 - 30);


        primaryStage.setTitle("Pong");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        root.requestFocus();

//        -----------Controls---------

        Controller controller = new Controller(scene, scoreboard, ball, leftPaddle, rightPaddle);
        controller.play();

    }
}
