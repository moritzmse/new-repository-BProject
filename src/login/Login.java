package login;

import internDatabase.ManageSQLite;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Login extends Application{

    public TextField password_input;
    public Button login_button;

    @FXML
    private void login(){
        if(ManageSQLite.login(password_input.getText())) {
            try {
                Stage stage = (Stage) login_button.getScene().getWindow();
                stage.close();

                Parent root = FXMLLoader.load(getClass().getResource("/main/mainScreen.fxml"));
                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.show();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
