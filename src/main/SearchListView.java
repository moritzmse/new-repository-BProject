package main;

import database.TempDatabase;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SearchListView extends Application {
    @FXML
    TableView<ObservableList<String>> tableView;//= new TableView<>();

//    public void method() {
//       // SearchListView.launch();
//        TableColumn<String, String> tableColumn = new TableColumn<>("Marke");
//        tableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
//
//        tableView.getColumns().add(tableColumn);
//        ObservableList<String> items = FXCollections.observableArrayList("abc", "abc2", "abc3", "abc4", "abc5");
//        tableView.setItems(items);
//
//
//
//
//        TableColumn<String, String> tableColumn2 = new TableColumn<>("Preis");
//        tableColumn2.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
//
//        tableView.getColumns().add(tableColumn2);
//       // ObservableList<String> items2 = FXCollections.observableArrayList(
//       //         ("AMK","HRSN"), new ("MFK"));
//      //  tableView.setItems(items2);
//
//    }

    public void onClickListener(){
        
    }

    public void method(){
//        SearchListView.launch();

//        ObservableList[] helps = new ObservableList[TempDatabase.jude.ColumnLength];

        //Columns hinzufügen
        for(int i = 0; i < TempDatabase.jude.ColumnLength; i++) {
            final int index = i;
            TableColumn<ObservableList<String>, String> tableColumn = new TableColumn<>(TempDatabase.jude.ColumnNames[i]);
            tableColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>((cellData.getValue().get(index))));
            tableView.getColumns().add(tableColumn);
            //ende

            //tableColumn.setCellValueFactory(new PropertyValueFactory<>(TempDatabase.jude.ColumnNames[i]));





            //    helps[i] = items;

            //System.out.println("HRS :"+items.toString());

            //tableView.setItems(items);
            //RICHTIG:  tableView.getItems().add(items);
            //tableView.getColumns().add(tableColumn);

            //ObservableList helps = tableView.getItems();
/*

        for(int i = 0; i < TempDatabase.jude.ColumnLength; i++){

            TableColumn tableColumn = new TableColumn(TempDatabase.jude.ColumnNames[i]);
            tableColumn.setCellValueFactory(new PropertyValueFactory<>(TempDatabase.jude.ColumnNames[i]));

            tableView.setItems();
  */
        }

        ObservableList<String>[] helps = new ObservableList[TempDatabase.jude.Values.size()];

        for(int i=0; i<TempDatabase.jude.Values.size(); i++) {
            ObservableList<String> items = FXCollections.observableArrayList();
            for (int j = 0; j < TempDatabase.jude.ColumnLength; j++) {
                Object[] helper = TempDatabase.jude.Values.get(i);

                String help = ((SimpleStringProperty) helper[j]).getBean().toString();

                System.out.println("ITEM: " + help);
                items.add(help);
                helps[i] = items;
                System.out.println("HRS :"+items.toString());
            }
        }

        //data hinzufügen
        for ( int l = 0; l < TempDatabase.jude.Values.size(); l++) {
            System.out.println("FML :" + helps[l]);
            tableView.getItems().add(helps[l]);
        }



        //TableColumn<String, String> tableColumn = new TableColumn<>("Marke");
        //tableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));

        //tableView.getColumns().add(tableColumn);
        //ObservableList<String> items = FXCollections.observableArrayList(TempDatabase.jude.ColumnNames[0]); //FXCollections.observableArrayList("abc", "abc2", "abc3", "abc4", "abc5");
        //tableView.setItems(items);
    }

    @Override
    public void start(Stage primaryStage) {
        //  method();
        System.out.println("Test 1");

        tableView = new TableView<>();
        TableColumn<String, String> tableColumn = new TableColumn<>("Name");

        tableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));

        //   method();
        System.out.println("Test 2");

        //  tableView.getColumns().add(tableColumn);
        //  ObservableList<String> items = FXCollections.observableArrayList("abc");
        //  tableView.setItems(items);

        //  method();
        System.out.println("Test 3");

        VBox root = new VBox(tableView);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();

        method();
        System.out.println("Test 4");
    }
}