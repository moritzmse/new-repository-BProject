package main;

import database.MariaDB_Commands;
import database.MariaDB_Connection;
import database.SearchValues;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        primaryStage.setTitle("MainPage");
        primaryStage.setScene(new Scene(root, 600, 450));
        primaryStage.show();

        /*List<Object[]> helper = MariaDB_Commands.normalSearch("Vittel");
        System.out.println(helper.get(0)[1]);//*/
        /*SearchValues helper = MariaDB_Commands.normalSearch("Vittel");
        System.out.println(helper.ColumnLength);
        for(int i = 0; i < helper.ColumnLength; i++){
            System.out.println(helper.ColumnNames[i]);
        }//*/
    }


    public static void main(String[] args) {
        launch(args);
    }
}
