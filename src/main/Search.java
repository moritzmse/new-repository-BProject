package main;

import database.MariaDB_Commands;
import database.MariaDB_Search;
import database.SearchValues;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private CheckBox[] reseller_checkbox;

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
            }
        }else{
            System.out.println("Error (src/main/Search) nullPointer in SearchValues");
        }
    }


    @FXML
    private void search(){
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
        StringBuilder checkboxes;
        if(whereClause.length() > 29){
            checkboxes = new StringBuilder(" and Reseller in (");
        }else{
            checkboxes = new StringBuilder(" Reseller in (");
        }

        Boolean firstReseller = true;
        for(int i = 0; i < reseller_checkbox.length; i++){
            if(reseller_checkbox[i].isSelected()){
                if(firstReseller) {
                    checkboxes.append(reseller_checkbox[i].getText());
                    firstReseller = false;
                }else{
                    checkboxes.append(","+reseller_checkbox[i].getText());
                }
            }
        }
        checkboxes.append(") ");

        whereClause = whereClause+checkboxes.toString();


        //DatePickerFrom
        if(datePickerFrom.getEditor().getText().length() == 10){
            if(whereClause.length() > 29) {
                whereClause = whereClause + " and";
            }
            whereClause = whereClause + " Week >= " + getWeek(datePickerFrom);
        }

        //DatePickerTo
        if(datePickerTo.getEditor().getText().length() == 10){
            if(whereClause.length() > 29) {
                whereClause = whereClause + " and";
            }
            whereClause = whereClause + " Week <= " + getWeek(datePickerTo);
        }

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
}
