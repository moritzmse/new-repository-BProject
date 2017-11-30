package sample;

import database.MariaDB_Connection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.Statement;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        Statement statement = MariaDB_Connection.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM OVERVIEW WHERE Week = '201601'");
        int counter = 0;
        while(resultSet.next()) {
            counter++;
            System.out.println(resultSet.getString(1));
        }
        System.out.println("Gesamt:"+counter);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
