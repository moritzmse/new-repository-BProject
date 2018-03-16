package main;

import calculations.Calculations;
import database.MariaDB_Commands;
import database.MariaDB_Search;
import database.SearchValues;
import database.TempDatabase;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import warning.Warnings;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Search {

    public TextField search_productname;
    public TextField search_manufacturer, search_product, search_productgroup;
    public TextField search_unitprice, search_units, search_packprice;
    public DatePicker datePickerFrom, datePickerTo;
    public VBox search_resellerVbox;
    public TitledPane search_resellerPane;
    public AnchorPane Pane;
    public Button search_button;
    public VBox vboxMain;
    public Label MinPreis;
    public Label MaxPreis;
    public Label AvgPreis;
    public TabPane mainTabPane;
    private CheckBox[] reseller_checkbox;
    private static int offenerTab;

    private static List<SearchValues> posListe = new ArrayList<>();

    //TODO Asci überarbeiten
    public CheckBox Ab, Am, Aä, B, Bi, Bp, Cp, Dp, Es, Fd, H, I, K, Lg, ME, Mn, Mu, MW, Ne, O, OP, Pa, Rb, Rs, So, Sp, Ti, Tp, VS, Z, Zu;

    @FXML
    TableView<ObservableList<String>> tableView;

    private String whereConditions;

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
        //Attributen Listener hinzufügen
        //evtl. wenn möglich listener direkt in fxml hinterlegen mit values (sieht aber schlecht aus -> vmtl. nicht möglich)
        CheckBox[] helper = {Ab, Am, Aä, B, Bi, Bp, Cp, Dp, Es, Fd, H, I, K, Lg, ME, Mn, Mu, MW, Ne, O, OP, Pa, Rb, Rs, So, Sp, Ti, Tp, VS, Z, Zu};
        for(int i = 0; i < helper.length; i++){
            int finalI = i;
            helper[i].setOnAction(event -> setAttributes(helper[finalI].getId(), helper[finalI].isSelected()));
        }
        //

        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);//nur eine Zeile kann ausgewaehlt werden //-->(SelectionMode.MULTIPLE); //mehrere Zeilen können ausgewählt werden

        //Doppelklick Methode löst getSelectedRowValues aus -> secondSearch
        tableView.setRowFactory( tv -> {
            TableRow<ObservableList<String>> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    //ObservableList<String> rowData = row.getItem();
                    //System.out.println(rowData);
                    getSelectedRowValues();
                }
            });
            return row ;
        });
    }

    //Wird unter dem Button suchen aufgerufen
    @FXML
    private void firstSearch() {

        final int whereLength = 1; //63; //Alt 29
        TextField[] search_values = new TextField[] {search_productname, search_manufacturer, search_product, search_productgroup, search_unitprice, search_units, search_packprice};

        StringBuilder whereClause = new StringBuilder("");

        if((search_productname.getText() != null && search_productname.getText().length() > 0) || checkDatePicker()) {
            for (TextField helpSearchValues : search_values) {
                if (helpSearchValues.getText() != null && helpSearchValues.getText().length() > 0) {
                    if (whereClause.length() > whereLength) {
                        whereClause.append(" and ").append(helpSearchValues.getId()).append(" like '%").append(helpSearchValues.getText()).append("%' ");
                    } else {
                        whereClause.append(helpSearchValues.getId()).append(" like '%").append(helpSearchValues.getText()).append("%' ");
                    }
                }
            }

            //ResellerCheckBox
            if (TempDatabase.ResellerWhere != null && TempDatabase.ResellerWhere.size() > 0) {
                StringBuilder checkboxes;
                if (whereClause.length() > whereLength) {
                    checkboxes = new StringBuilder(" and Reseller in (");
                } else {
                    checkboxes = new StringBuilder(" Reseller in (");
                }

                Boolean firstReseller = true;
                for (int i = 0; i < TempDatabase.ResellerWhere.size(); i++) {
                    if (firstReseller) {
                        checkboxes.append(TempDatabase.ResellerWhere.get(i));
                        firstReseller = false;
                    } else {
                        checkboxes.append(",").append(TempDatabase.ResellerWhere.get(i));
                    }
                }
                checkboxes.append(") ");

                whereClause.append(checkboxes.toString());

            }
            //ResellerCheckBox End

            //DatePickerFrom
            if (datePickerFrom.getEditor().getText().length() == 10) {
                if (whereClause.length() > whereLength) {
                    whereClause.append(" and");
                }
                whereClause.append(" Week >= ").append(getWeek(datePickerFrom));
            }
            //DatePickerFrom End

            //DatePickerTo
            if (datePickerTo.getEditor().getText().length() == 10) {
                if (whereClause.length() > whereLength) {
                    whereClause.append(" and");
                }
                whereClause.append(" Week <= ").append(getWeek(datePickerTo));
            }
            //DatePickerTo End


            //Attributes Start
            if (TempDatabase.AttributesWhere != null && TempDatabase.AttributesWhere.size() > 0) {
                for (int i = 0; i < TempDatabase.AttributesWhere.size(); i++) {
                    if (whereClause.length() > whereLength) {
                        whereClause.append(" and");
                    }
                    whereClause.append(" Attributes like '%").append(TempDatabase.AttributesWhere.get(i)).append("%'");
                }
            }
            //Attributes End


            //TODO evtl direkt whereClause durch whereConditions ersetzen? überlegen wie sich das auf weitere suchen auswirkt. -> whereConditions müsste immer oben in der firstSearchMethode = "" gesetzt werden
            whereConditions = whereClause.toString();
            SearchValues resultValues = MariaDB_Search.search("SELECT DISTINCT Manufacturer,Brand,Product FROM OVERVIEW WHERE " + whereConditions);

            TempDatabase.searchValues = resultValues;

            if (resultValues != null) {
                showResults();
            } else {
                //TODO evtl. ne eigene clearMethode???
                tableView.getColumns().clear();
                tableView.getItems().clear();
            }
        }else{
            Warnings.warngingEmptySearch();
        }
    }

    //Wird von getSelectedRowValues ausgerufen
    private void secondSearch(ArrayList<String> selectedRowsArray) {

        String selectedWheres = " and Manufacturer = '" + selectedRowsArray.get(TempDatabase.manufacturerPosition) + "'" +
                " and Brand = '" + selectedRowsArray.get(TempDatabase.brandPosition) + "'" +
                " and Product = '" + selectedRowsArray.get(TempDatabase.productPosition) + "'";

        TempDatabase.searchValues = MariaDB_Search.search("SELECT * FROM OVERVIEW WHERE " + whereConditions + selectedWheres);



        //TODO Hier die Methode rein, welche den Graphen anzeigt.

        showGraph();

        MaxPreis.setText("MaxPreis: " + String.valueOf(Calculations.calculateMaxPreis(TempDatabase.searchValues)) + "€");
        MinPreis.setText("MinPreis: " + String.valueOf(Calculations.calculateMinPreis(TempDatabase.searchValues))+ "€");
        AvgPreis.setText("AvgPreis: " + String.valueOf(Calculations.calculateAvgPreis(TempDatabase.searchValues))+ "€");

       Calculations.countAttribute();

        //Idee: nach der Suche die Filter auf enabled flase setzen, dadurch wird filter ändern gesperrt, bevor ein genaues produkt gewählt wird. der neue filter wird da nämlich nicht beachtet werden
        //Oder sobald ein Filter geändert wird die Ergebnissliste Resetten, damit erst neu "richtig" gesucht werden muss.
    }

    private void showResults(){
        //Switch zur Ergebnisliste
        mainTabPane.getSelectionModel().select(0);

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

                //System.out.println("ITEM: " + help);
                items.add(help);
                helps[i] = items;
                //System.out.println("HRS :"+items.toString());
            }
        }

        //data hinzufügen
        for (int l = 0; l < TempDatabase.searchValues.Values.size(); l++) {
            //System.out.println("FML :" + helps[l]);
            tableView.getItems().add(helps[l]);
        }
    }

    private void getSelectedRowValues(){ //ausgewaehlte Zeilen/Zeile der Tabelle ausgeben //neu: neue Suchanfrage starten zu ausgewählter Zeile für restliche Einträge
        ArrayList<String> selectedRowsArray = new ArrayList<>();

        if(tableView.getSelectionModel().getSelectedItem()!=null){
            for(int i = 0; i < tableView.getSelectionModel().getSelectedItem().size(); i++){  //Spalten der ausgewählte Zeile in Array schreiben
                selectedRowsArray.add(tableView.getSelectionModel().getSelectedItem().get(i));
            }
            secondSearch(selectedRowsArray);
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

    private static void setAttributes(String attributes, boolean add){
        if(TempDatabase.AttributesWhere == null){
            TempDatabase.AttributesWhere = new ArrayList();
            TempDatabase.AttributesWhere.add(attributes);
        }else{
            if(add){
                TempDatabase.AttributesWhere.add(attributes);
            }else{
                TempDatabase.AttributesWhere.remove(""+attributes);
            }
        }
    }

    @FXML
    public void enterListener(KeyEvent e) throws IOException {
        if(e.getCode().equals(KeyCode.ENTER)){
            //TODO mit Markus abklären ob der Listener sich auf die suchleiste bezieht oder die ergebnis Tabelle
            firstSearch();
        }
    }

    //TODO überhaupt noch nötig?????????
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

    private void showGraph(){
        if(TempDatabase.searchValues!=null){
            //neuer main tab
            mainTabPane.getTabs().add(new Tab());
            int i = mainTabPane.getTabs().size();
            Tab tab = mainTabPane.getTabs().get(i-1);
            tab.setText(Calculations.getProductName() + " ...");
            //second tabs/graphen
            TabPane secondPane = new TabPane();
            tab.setContent(secondPane);
            chartTabs(secondPane);
            //
            posListe.add(TempDatabase.searchValues);
            offenerTab = i-1;
            mainTabPane.getSelectionModel().select(mainTabPane.getTabs().size()-1);
        }
    }

    private void chartTabs(TabPane secondpane){
        int i;
        try {
            //linechart
            secondpane.getTabs().add(FXMLLoader.load(this.getClass().getResource("/graph/graphPaneLineChart.fxml")));
            i = secondpane.getTabs().size();
            Tab tabLineChart = secondpane.getTabs().get(i-1);
            tabLineChart.setClosable(false);
            tabLineChart.setText("Preisverlauf");
            //attribute
            secondpane.getTabs().add(FXMLLoader.load(this.getClass().getResource("/graph/graphPaneBarChart.fxml")));
            i = secondpane.getTabs().size();
            Tab tabBarChart = secondpane.getTabs().get(i-1);
            tabBarChart.setClosable(false);
            tabBarChart.setText("Attributverteilung");
            //Verteilung Preise
            secondpane.getTabs().add(FXMLLoader.load(this.getClass().getResource("/graph/graphPanePriceAmount.fxml")));
            i = secondpane.getTabs().size();
            Tab tabPriceAmount = secondpane.getTabs().get(i-1);
            tabPriceAmount.setClosable(false);
            tabPriceAmount.setText("Preisverteilung");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkDatePicker(){
        return (datePickerFrom.getEditor().getText() != null && datePickerFrom.getEditor().getText().length() >= 8
                && datePickerTo.getEditor().getText() != null && datePickerTo.getEditor().getText().length() >= 8);
    }

    @FXML
    private int getTab(){
        offenerTab = mainTabPane.getSelectionModel().getSelectedIndex();
        int i = mainTabPane.getSelectionModel().getSelectedIndex();
        if(i>0){
            MaxPreis.setText(String.valueOf("MaxPreis: " +Calculations.calculateMaxPreis(posListe.get(i-1)))+ "€");
            MinPreis.setText(String.valueOf("MinPreis: " +Calculations.calculateMinPreis(posListe.get(i-1)))+ "€");
            AvgPreis.setText(String.valueOf("AvgPreis: " +Calculations.calculateAvgPreis(posListe.get(i-1)))+ "€");
        }
        System.out.println(offenerTab);
        System.out.println(posListe.size());
        return mainTabPane.getSelectionModel().getSelectedIndex();
    }

    //TODO Warning !!!! wird die Methode überhaupt benötigt?????
    @FXML
    public void removeObjectFromPosList(){
        if(offenerTab>0){
            /*if(posListe.size() == 1){     //wenn alle tabs geschlossen sind: Werte unten ausblenden -- Fehler?
                //MaxPreis.setText(" ");
                System.out.println(MaxPreis.getText());
            }*/
          /*  if(offenerTab-1==0){          //wenn tab 0 = offener Tab ist: Werte unten ausblenden
                //MaxPreis.setText(" ");
                MinPreis.setText(" ");
            }*/
        posListe.remove(offenerTab-1);
        offenerTab = (offenerTab-1);
        } else {System.out.println("test");}
    }

    //Auf das Löschen von Tabs reagieren!!!!!!!!!!!!!
    //Bei "Suchen" wieder in Ergebnis-Tab springen
    //get tabs methode überarbeiten
    //wenn alle Tabs geschlossen sind: Max, Min, etc Berechnungen ausblenden


    @FXML
    private void logout(){
        try {
            Stage stage = (Stage) search_product.getScene().getWindow();
            stage.close();

            Parent root = FXMLLoader.load(getClass().getResource("/login/login.fxml"));
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void passwordSettings(){
        try {
            Stage stage = new Stage();

            Parent root = FXMLLoader.load(getClass().getResource("/settings/settingsPassword.fxml"));
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
