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
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Search {

    public TextField search_productname;
    public TextField search_manufacturer, search_brand, search_product, search_productgroup;
    public TextField search_unitprice, search_units, search_packprice;
    public DatePicker datePickerFrom, datePickerTo;
    public VBox search_resellerVbox;
    public TitledPane search_resellerPane;
    public AnchorPane Pane;
    public Button search_button;
    public VBox vboxMain;

    private CheckBox[] reseller_checkbox;

    private final int whereLength = 63; //Alt 29

    //TODO Asci überarbeiten
    public CheckBox Ab, Am, Aä, B, Bi, Bp, Cp, Dp, Es, Fd, H, I, K, Lg, ME, Mn, Mu, MW, Ne, O, OP, Pa, Rb, Rs, So, Sp, Ti, Tp, VS, Z, Zu;

    @FXML LineChart<String, Number> lineChart;

    @FXML
    TableView<ObservableList<String>> tableView;//= new TableView<>();

    @FXML
    Text textCalculation;



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
                reseller_checkbox[i].setOnAction(event -> setReseller(Integer.parseInt(reseller_checkbox[finalI].getText()),reseller_checkbox[finalI].isSelected()));
            }
        }else{
            System.out.println("Error (src/main/Search) nullPointer in SearchValues");
        }


        //TODO ggf. kürzen?
        //Ersetzten durch Methode die im fxml hinterlegt wird -> Attribute set fxml kompatibel machen.
        //TODO Verbuggt desshalb erstmal durch andere Methode ersetzt
        /*CheckBox[] helper = {Ab, Am, Aä, B, Bi, Bp, Cp, Dp, Es, Fd, H, I, K, Lg, ME, Mn, Mu, MW, Ne, O, OP, Pa, Rb, Rs, So, Sp, Ti, Tp, VS, Z, Zu};
        for(int i = 0; i < helper.length; i++){
            int finalI = i;
            helper[i].setOnAction(event -> setAttributes(helper[finalI].getId(), helper[finalI].isSelected()));
        }*/

        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);//nur eine Zeile kann ausgewaehlt werden //-->(SelectionMode.MULTIPLE); //mehrere Zeilen können ausgewählt werden

        /*tableView.setOnMousePressed(new EventHandler<MouseEvent>() {        //Auf Doppelklick Zeile zurückgeben
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    System.out.println(tableView.getSelectionModel().getSelectedItem());
                    Object selectedItem = tableView.getSelectionModel().getSelectedItem();
                    getSelectedRowValues(selectedItem);
                    //... Suche aufrufen mit ausgewähltem Produkt + Filtern; Graph generieren etc
                }
            }
        });*/

        tableView.setRowFactory( tv -> {
            TableRow<ObservableList<String>> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    ObservableList<String> rowData = row.getItem();
                    System.out.println(rowData);
                    getSelectedRowValues(row);
                }
            });
            return row ;
        });

    }

    @FXML
    private void search() {
        TextField[] search_values = new TextField[] {search_productname, search_manufacturer, search_brand, search_product, search_productgroup, search_unitprice, search_units, search_packprice};

        //WHERE Clause für die erste Abfrage
        String whereClause = "SELECT DISTINCT Manufacturer,Brand,Product FROM OVERVIEW WHERE ";

        //WHERE Clause wenn ein Feld ausgewählt wurde
        //String whereClause = "SELECT * FROM OVERVIEW WHERE ";

        for(int i = 0; i < search_values.length; i++){
            if(search_values[i].getText() != null && search_values[i].getText().length() > 0){
                if(whereClause.length() > whereLength){
                    whereClause = whereClause + " and " + search_values[i].getId() + " like '%" + search_values[i].getText() + "%' ";
                }else{
                    whereClause = whereClause + search_values[i].getId() + " like '%" + search_values[i].getText() + "%' ";
                }
            }
        }

        //ResellerCheckBox
        if(TempDatabase.ResellerWhere != null && TempDatabase.ResellerWhere.size() > 0){
            StringBuilder checkboxes;
            if(whereClause.length() > whereLength){
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
            if(whereClause.length() > whereLength) {
                whereClause = whereClause + " and";
            }
            whereClause = whereClause + " Week >= " + getWeek(datePickerFrom);
        }
        //DatePickerFrom End

        //DatePickerTo
        if(datePickerTo.getEditor().getText().length() == 10){
            if(whereClause.length() > whereLength) {
                whereClause = whereClause + " and";
            }
            whereClause = whereClause + " Week <= " + getWeek(datePickerTo);
        }
        //DatePickerTo End


        //Attributes Start
        CheckBox[] checkbox_helper = {Ab, Am, Aä, B, Bi, Bp, Cp, Dp, Es, Fd, H, I, K, Lg, ME, Mn, Mu, MW, Ne, O, OP, Pa, Rb, Rs, So, Sp, Ti, Tp, VS, Z, Zu};
        for(int i = 0; i < checkbox_helper.length; i++){
            if(checkbox_helper[i].isSelected()) {
                if (whereClause.length() > whereLength) {
                    whereClause = whereClause + " and";
                }
                whereClause = whereClause + " Attributes like '%" + checkbox_helper[i].getId() + "%'";
            }
        }
        //Attributes End

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
        //System.out.println("JEDER HERNER IST EIN HRSN");

        TempDatabase.searchValues = resultValues;
       if(resultValues != null){
           showResults();
       }
    }

    private void showResults(){
        //Sucheregbnisse in Tabelle anzeigen

        //Tabelle leeren
        tableView.getColumns().clear();
        tableView.getItems().clear();

        //Columns hinzufügen
        for(int i = 0; i < TempDatabase.searchValues.ColumnLength; i++) {
            final int index = i;
            TableColumn<ObservableList<String>, String> tableColumn = new TableColumn<>(TempDatabase.searchValues.ColumnNames[i]);
            tableColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>((cellData.getValue().get(index))));
            tableView.getColumns().add(tableColumn);
        }

        //item-liste befuellen
        ObservableList<String>[] helps = new ObservableList[TempDatabase.searchValues.Values.size()];

        for(int i = 0; i<TempDatabase.searchValues.Values.size(); i++) {
            ObservableList<String> items = FXCollections.observableArrayList();
            items.clear();
            for (int j = 0; j < TempDatabase.searchValues.ColumnLength; j++) {
                Object[] helper = TempDatabase.searchValues.Values.get(i);

                String help = ((SimpleStringProperty) helper[j]).getBean().toString();

                System.out.println("ITEM: " + help);
                items.add(help);
                helps[i] = items;
                System.out.println("HRS :"+items.toString());
            }
        }

        //data hinzufügen
        for (int l = 0; l < TempDatabase.searchValues.Values.size(); l++) {
            System.out.println("FML :" + helps[l]);
            tableView.getItems().add(helps[l]);
        }
    }




    public ArrayList<String> getSelectedRowValues(TableRow row){ //ausgewaehlte Zeilen/Zeile der Tabelle ausgeben //neu: neue Suchanfrage starten zu ausgewählter Zeile für restliche Einträge
        ArrayList<String> selectedRowsArray = new ArrayList<String>();
        TableRow<ObservableList<String>> selectedItems =  row;

        if(tableView.getSelectionModel().getSelectedItem()!=null){
                     // Object selectedItems = tableView.getSelectionModel().getSelectedItems(); //Problem mit Kommata

            //for(int i=0; i<tableView.getSelectionModel().getSelectedItems().size(); i++) { //i = Anzahl ausgewählter Zeilen
            //String column = selectedItems.toString().split(",")[i].substring(1);
            //System.out.println("Zeilen : " + column);
            for(int i = 0; i < tableView.getSelectionModel().getSelectedItem().size(); i++){  //Spalten der ausgewählte Zeile in Array schreiben
                selectedRowsArray.add(tableView.getSelectionModel().getSelectedItem().get(i));
            }

            //String selectedRows = selectedItems.toString();           //unnoetig: nur noch eine Zeile kann gleichzeitig ausgewaehlt werden
            //System.out.println("alle zeilen : " + selectedRows);
            //String[] columnSplit = selectedRows.split("\\[");

            /*for(int j=0; j<columnSplit.length; j++){
                System.out.println(columnSplit.length);
                System.out.println("split: " + columnSplit[j]);
                if(columnSplit[j].isEmpty()){
                    //wenn leer: nichts tun
                }
                else{
                    selectedRowsArray.add(columnSplit[j]); //Tabellenzeilen zur Liste hinzufuegen
                }
            }*/


                /*for(int k=0; k<selectedRowsArray.size(); k++){
                    System.out.println("selectedRows Array: "+k + " _ " +selectedRowsArray.get(k));
                }*/
            //return selectedRows;

            //Aufruf der Graphengenerierung
            /*try {
               // generateLineChart(selectedRowsArray); //Graph generieren //wieder aktivieren
               // displayTextCalculation(selectedRowsArray); //Text mit Berechnungen anzeigen //muss ueberarbeitet werden
            } catch (ParseException e) {
                e.printStackTrace();
            }*/
            additionalSearch(selectedRowsArray);
            return selectedRowsArray;
        }
        else{
            return null;
        }
    }

    private void additionalSearch( ArrayList<String> selectedRowsArray){
        int whereLength2 = 29;
        TextField[] search_values = new TextField[] {search_productname, search_manufacturer, search_brand, search_product, search_productgroup, search_unitprice, search_units, search_packprice};
        search_productname.setText(selectedRowsArray.get(TempDatabase.brandPosition));//oberstes Suchfeld, brand
        search_manufacturer.setText(selectedRowsArray.get(TempDatabase.manufacturerPosition));//manufacturer
        search_brand.setText(selectedRowsArray.get(TempDatabase.brandPosition));//search_brand
        search_product.setText(selectedRowsArray.get(TempDatabase.productPosition));

        //WHERE Clause für die erste Abfrage
        String whereClause = "SELECT * FROM OVERVIEW WHERE ";

        //WHERE Clause wenn ein Feld ausgewählt wurde
        //String whereClause = "SELECT * FROM OVERVIEW WHERE ";

        for(int i = 0; i < search_values.length; i++){
            System.out.println("search value ids: " + search_values[i].getId());
            if(search_values[i].getText() != null && search_values[i].getText().length() > 0){
                if(whereClause.length() > whereLength2){
                    whereClause = whereClause + " and " + search_values[i].getId() + " like '%" + search_values[i].getText() + "%' ";
                }else{
                    whereClause = whereClause + search_values[i].getId() + " like '%" + search_values[i].getText() + "%' ";
                }
            }
        }

       // if(search_values[TempDatabase.productPosition].getText() != null && search_values[TempDatabase.productPosition].getText().length() > 0){
        //}


        //ResellerCheckBox
        if(TempDatabase.ResellerWhere != null && TempDatabase.ResellerWhere.size() > 0){
            StringBuilder checkboxes;
            if(whereClause.length() > whereLength2){
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
            if(whereClause.length() > whereLength2) {
                whereClause = whereClause + " and";
            }
            whereClause = whereClause + " Week >= " + getWeek(datePickerFrom);
        }
        //DatePickerFrom End

        //DatePickerTo
        if(datePickerTo.getEditor().getText().length() == 10){
            if(whereClause.length() > whereLength2) {
                whereClause = whereClause + " and";
            }
            whereClause = whereClause + " Week <= " + getWeek(datePickerTo);
        }
        //DatePickerTo End


        //Attributes Start
        CheckBox[] checkbox_helper = {Ab, Am, Aä, B, Bi, Bp, Cp, Dp, Es, Fd, H, I, K, Lg, ME, Mn, Mu, MW, Ne, O, OP, Pa, Rb, Rs, So, Sp, Ti, Tp, VS, Z, Zu};
        for(int i = 0; i < checkbox_helper.length; i++){
            if(checkbox_helper[i].isSelected()) {
                if (whereClause.length() > whereLength2) {
                    whereClause = whereClause + " and";
                }
                whereClause = whereClause + " Attributes like '%" + checkbox_helper[i].getId() + "%'";
            }
        }
        //Attributes End

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

        TempDatabase.searchValues = resultValues;
        if(resultValues != null){
            //showResults();
            System.out.println("knorf knorf: " + TempDatabase.searchValues.Values.get(0));
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

            //Schleife wird benötigt, da sonst die Woche 53 im Januar dem aktuellen Jahr zugewiesen wird z.B. 1.1.2016 = 201653 -> wäre falsch
            if(Integer.parseInt(week) > 10 && cal.get(Calendar.MONTH) < 2 ){
                year = year - 1;
            }
            //Ende der benötigten Schleife

            if (week.length() == 1) {
                week = 0 + week;
            }

            return year+week;
        }catch (ParseException e){
            System.out.println("Error (src/main/Search) getWeek false DateFormat");
            return null;
        }
    }


    public void generateLineChart(ArrayList<String> selectedRowValues) throws ParseException {
        //Muss geändert werden: X-Achse Monate, Y-Achse Preise, ...
        //soll  TempDatabase.searchValues nutzen
        lineChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
//        series.getData().add(new XYChart.Data<String, Number>("Name", 100));
//        series.setName("Werte");
//        lineChart.getData().add(series);

        for(int i=0; i<selectedRowValues.size(); i++){

            //String[] elementList = selectedRowValues.get(i).split(",[ ]*");
            //String string = elementList[2];
            String string = selectedRowValues.get(TempDatabase.productPosition);  //Produktname
            //Number number = NumberFormat.getInstance().parse(elementList[6]);
            Number number = NumberFormat.getInstance().parse(selectedRowValues.get(TempDatabase.pricePosition)); //Unit Price
            //String date = elementList[4];
            String date = selectedRowValues.get(TempDatabase.weekPosition);
                Date d = new SimpleDateFormat("yyyyww").parse(date);
                String output = new SimpleDateFormat("yyyy-ww").format(d);
                //System.out.println("output = " + output);

            series.getData().add(new XYChart.Data<String, Number>(output, number));
        }
        //String[] elementList = selectedRowValues.get(0).split(",[ ]*");
        //String productName = elementList[2];
        String productName = selectedRowValues.get(TempDatabase.productPosition);
        series.setName(productName/*"Product"*/);
        lineChart.getData().add(series);
    }

    public void displayTextCalculation(ArrayList<String> selectedRowValues){
        //Durchschnittspreis:
        double helper = 0;
        for(int i=0; i<selectedRowValues.size(); i++){
            String[] elementList = selectedRowValues.get(i).split(",[ ]*");
            String string = elementList[2];
            Double price = Double.parseDouble(elementList[6]);
            helper+=price;
        }
        double avgPrice = helper/selectedRowValues.size();
        textCalculation.setText("Durchschnittspreis für " + /*string +*/ "Produkt" + ": " + avgPrice);
    }

    private static void setReseller(int ResellerID, boolean add){
        if(TempDatabase.ResellerWhere == null){
            //TODO Theoretischer Bugg möglich wenn das Programm mit nem bugg starten sollte.
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

    @FXML
    public void enterListener(KeyEvent e) throws IOException {
        if(e.getCode().equals(KeyCode.ENTER)){
            search();
        }
    }
    public void enterListenerExtended(KeyEvent e){
       if(e.getCode().equals(KeyCode.ENTER)){
            for(int i = 0; i < vboxMain.getChildren().size();i++){
                if(vboxMain.getChildren().get(i).isFocused() && vboxMain.getChildren().get(i+2) instanceof TextField && i+2<vboxMain.getChildren().size()){
                    vboxMain.getChildren().get(i+2).requestFocus();
                    break;
                }
            }
        }
    }

    /*private static void setAttributes(String attributes, boolean add){
        System.out.println("AMK :"+attributes);
        if(TempDatabase.AttributesWhere == null){
            TempDatabase.ResellerWhere = new ArrayList();
            TempDatabase.AttributesWhere.add(attributes);
        }else{
            if(add){
                TempDatabase.AttributesWhere.add(attributes);
            }else{
                TempDatabase.AttributesWhere.remove(""+attributes);
            }
        }
    }*/
}
