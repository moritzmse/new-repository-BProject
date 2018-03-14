package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        primaryStage.setTitle("MainPage");
        primaryStage.setScene(new Scene(root, 1000, 750));
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/images/Logo.png")));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
