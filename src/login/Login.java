package login;

import internDatabase.ManageSQLite;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;

public class Login extends Application{

    public PasswordField password_input;
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
                stage.setTitle("MainPage");
                stage.getIcons().add(new Image(Main.class.getResourceAsStream("/images/Logo.png")));
                stage.show();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void enterListener(KeyEvent e) {
        if(e.getCode().equals(KeyCode.ENTER)){
            login();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/images/Logo.png")));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
