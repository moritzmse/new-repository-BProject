package login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Login {

    public TextField password_input;
    public Button login_button;

    @FXML
    private void login(){
        if(password_input.getText().equals("1234")) {
            try {
                Stage stage = (Stage) login_button.getScene().getWindow();
                stage.close();

                Parent root = FXMLLoader.load(getClass().getResource("/sample/sample.fxml"));
                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.show();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
