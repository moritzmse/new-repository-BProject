package settings;

import internDatabase.ManageSQLite;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import warning.Warnings;

public class SettingsPassword {

    public PasswordField oldPassword;
    public PasswordField newPasswordOne;
    public PasswordField newPasswordTwo;

    public void initialize(){

    }

    @FXML
    private void changePassword(){
        if(newPasswordOne.getText().equals(newPasswordTwo.getText())){
            if(ManageSQLite.setPassword(oldPassword.getText(), newPasswordOne.getText())){
                Warnings.warningSuccessfulPassword();
            }else{
                Warnings.warningErrorPasswordOld();
            }
        }else{
            Warnings.warningErrorPasswordNew();
        }
    }
}
