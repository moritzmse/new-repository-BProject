package main;

import database.MariaDB_Search;
import database.SearchValues;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Search {

    public TextField search_productname;
    public TextField search_manufacturer, search_brand, search_product, search_productgroup;
    public ChoiceBox search_reseller;
    public TextField search_unitprice, search_units, search_packprice;

    @FXML
    private void search(){

        TextField[] search_values = new TextField[] {search_productname, search_manufacturer, search_brand, search_product, search_productgroup, search_unitprice, search_units, search_packprice};


        String whereClause = "SELECT * FROM OVERVIEW WHERE ";

        for(int i = 0; i < search_values.length; i++){
            if(search_values[i].getText() != null && search_values[i].getText().length() > 0){
                if(whereClause.length() > 29){
                    whereClause = whereClause + "and " + search_values[i].getId() + " like '%" + search_values[i].getText() + "%' ";
                }else{
                    whereClause = whereClause + search_values[i].getId() + " like '%" + search_values[i].getText() + "%' ";
                }
            }
        }

        System.out.println(whereClause);

        SearchValues resultValues = MariaDB_Search.search(whereClause);

        List<Object[]> values = resultValues.Values;

        values.size();
        for(int i = 0; i < values.size(); i++){
            Object[] help = values.get(i);

            System.out.println("FML: "+values.size());
            System.out.print(i+1+" :");
            for(int j = 0; j < resultValues.ColumnLength; j++) {
                StringProperty abs = (StringProperty) help[j];
                System.out.print(abs.getBean());
            }
        }

        System.out.println("JEDER HERNER IST EIN HRSN");
    }
}
