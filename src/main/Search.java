package main;

import database.MariaDB_Commands;
import database.MariaDB_Search;
import database.SearchValues;
import database.TempDatabase;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Search {

    public TextField search_productname;
    public TextField search_manufacturer, search_brand, search_product, search_productgroup;
    public TextField search_unitprice, search_units, search_packprice;
    public DatePicker datePickerFrom, datePickerTo;
    public VBox search_resellerVbox;
    public TitledPane search_resellerPane;
    public AnchorPane Pane;

    private CheckBox[] reseller_checkbox;

    @FXML
    TableView<ObservableList<String>> tableView;//= new TableView<>();

    public void initialize(){
        SearchValues resultValues = MariaDB_Commands.resellerSearch();

        if(resultValues != null) {
            List<Object[]> values = resultValues.Values;

            reseller_checkbox = new CheckBox[values.size()];

            for (int i = 0; i < values.size(); i++) {
                Object[] name = values.get(i);
                SimpleStringProperty hrs = (SimpleStringProperty) name[0];
                reseller_checkbox[i] = new CheckBox(hrs.getBean().toString());
                search_resellerVbox.getChildren().add(reseller_checkbox[i]);
                int finalI = i;
                reseller_checkbox[i].setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        setReseller(Integer.parseInt(reseller_checkbox[finalI].getText()),reseller_checkbox[finalI].isSelected());
                    }
                });
            }
        }else{
            System.out.println("Error (src/main/Search) nullPointer in SearchValues");
        }

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    private void search() throws IOException {

        //  javafx.scene.layout.Pane newLoadedPane =        FXMLLoader.load(getClass().getResource("SearchListViewFXML.fxml"));
        //  Pane.getChildren().add(newLoadedPane);
        //  AnchorPane.setTopAnchor(newLoadedPane,0.0);
        //  AnchorPane.setBottomAnchor(newLoadedPane, 0.0);
        //  AnchorPane.setLeftAnchor(newLoadedPane, 0.0);
        //  AnchorPane.setRightAnchor(newLoadedPane, 0.0);

        TextField[] search_values = new TextField[] {search_productname, search_manufacturer, search_brand, search_product, search_productgroup, search_unitprice, search_units, search_packprice};

        String whereClause = "SELECT * FROM OVERVIEW WHERE ";

        for(int i = 0; i < search_values.length; i++){
            if(search_values[i].getText() != null && search_values[i].getText().length() > 0){
                if(whereClause.length() > 29){
                    whereClause = whereClause + " and " + search_values[i].getId() + " like '%" + search_values[i].getText() + "%' ";
                }else{
                    whereClause = whereClause + search_values[i].getId() + " like '%" + search_values[i].getText() + "%' ";
                }
            }
        }

        //ResellerCheckBox
        if(TempDatabase.ResellerWhere != null && TempDatabase.ResellerWhere.size() > 0){
            StringBuilder checkboxes;
            if(whereClause.length() > 29){
                checkboxes = new StringBuilder(" and Reseller in (");
            }else{
                checkboxes = new StringBuilder(" Reseller in (");
            }

            Boolean firstReseller = true;
            for(int i = 0; i < TempDatabase.ResellerWhere.size(); i++){
                if(firstReseller) {
                    checkboxes.append(TempDatabase.ResellerWhere.get(i));
                    firstReseller = false;
                }else{
                    checkboxes.append(","+TempDatabase.ResellerWhere.get(i));
                }
            }
            checkboxes.append(") ");

            whereClause = whereClause+checkboxes.toString();

        }
        //ResellerCheckBox End

        //DatePickerFrom
        if(datePickerFrom.getEditor().getText().length() == 10){
            if(whereClause.length() > 29) {
                whereClause = whereClause + " and";
            }
            whereClause = whereClause + " Week >= " + getWeek(datePickerFrom);
        }
        //DatePickerFrom End

        //DatePickerTo
        if(datePickerTo.getEditor().getText().length() == 10){
            if(whereClause.length() > 29) {
                whereClause = whereClause + " and";
            }
            whereClause = whereClause + " Week <= " + getWeek(datePickerTo);
        }
        //DatePickerTo End

        System.out.println(whereClause);

        SearchValues resultValues = MariaDB_Search.search(whereClause);

        if(resultValues != null) {
            List<Object[]> values = resultValues.Values;

            values.size();
            for (int i = 0; i < values.size(); i++) {
                Object[] help = values.get(i);

                System.out.println("FML: " + values.size());
                System.out.print(i + 1 + " :");
                for (int j = 0; j < resultValues.ColumnLength; j++) {
                    StringProperty abs = (StringProperty) help[j];
                    System.out.print(abs.getBean());
                }
            }
        }else{
            System.out.println("Error (src/main/Search) nullPointer in SearchValues");
        }
        System.out.println("JEDER HERNER IST EIN HRSN");

        TempDatabase.jude = resultValues;
        showResults();
    }

    public void showResults(){
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


    public void getSelectedRowValues(){
        if(tableView.getSelectionModel().getSelectedItem()!=null){
            Object selectedItems = tableView.getSelectionModel().getSelectedItems();
            //for(int i=0; i<tableView.getSelectionModel().getSelectedItems().size(); i++) { //i = Anzahl ausgewählter Zeilen
            //String column = selectedItems.toString().split(",")[i].substring(1);
            //System.out.println("Zeilen : " + column);
            String column = selectedItems.toString();
            System.out.println("alle zeilen : " + column);
            String[] columnSplit = column.split("\\[");

            for(int j=0; j<columnSplit.length; j++){
                System.out.println("split: " + columnSplit[j]);
            }
            //String column2 = tableView.getSelectionModel().getSelectedCells().toString();
            // System.out.println("..." + column2);
            //}
        }
    }

    private static String getWeek(DatePicker datePicker){
        try {
            String input = datePicker.getEditor().getText();
            String format = "dd.MM.yyyy";

            SimpleDateFormat df = new SimpleDateFormat(format);
            Date date = df.parse(input);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String week = "" + cal.get(Calendar.WEEK_OF_YEAR);
            int year = cal.get(Calendar.YEAR);

            if (week.length() == 1) {
                week = 0 + week;
            }

            return year+week;
        }catch (ParseException e){
            System.out.println("Error (src/main/Search) getWeek false DateFormat");
            return null;
        }
    }

    private static void setReseller(int ResellerID, boolean add){
        if(TempDatabase.ResellerWhere == null){
            TempDatabase.ResellerWhere = new ArrayList();
            TempDatabase.ResellerWhere.add(""+ResellerID);
        }else{
            if(add){
                TempDatabase.ResellerWhere.add(""+ResellerID);
            }else{
                TempDatabase.ResellerWhere.remove(""+ResellerID);
            }
        }
    }
}
