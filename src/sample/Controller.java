package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class Controller {


    @FXML
    Pane secPane;
    public void loadFxml (ActionEvent event) throws IOException {
        Pane newLoadedPane =        FXMLLoader.load(getClass().getResource("test.fxml"));
        secPane.getChildren().add(newLoadedPane);
    }

}
