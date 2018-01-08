package main;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import database.MariaDB_Search;
import database.SearchValues;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import main.Search;

public class SearchListView implements Initializable {



    @FXML
    private ListView myListView;
    @FXML
    private ListView myListView2;
    private Search testSearch;

    protected List<String> resultList = new ArrayList<>();
    protected List<String> resultList2 = new ArrayList<>();

    protected ListProperty<String> listProperty = new SimpleListProperty<>();
    protected ListProperty<String> listProperty2 = new SimpleListProperty<>();

    public TextField search_productname;
    public TextField search_manufacturer, search_brand, search_product, search_productgroup;
    public ChoiceBox search_reseller;
    public TextField search_unitprice, search_units, search_packprice;
    public DatePicker datePickerFrom, datePickerTo;



    @FXML
    private void handleButtonAction(ActionEvent event) {
        //search();
        listProperty.set(FXCollections.observableArrayList(resultList));
        listProperty2.set(FXCollections.observableArrayList(resultList2));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        SearchValues searchValues = MariaDB_Search.search("SELECT * FROM OVERVIEW WHERE Brand = 'Vittel'");
        //testSearch = new Search();
        //testSearch.getSearchValues().ColumnNames[0];

        resultList.add(searchValues.ColumnNames[1]);
        List<Object[]> help = searchValues.Values;

        for(int i = 0; i < help.size(); i++){
            Object[] helps = help.get(i);
            resultList.add((String) ((SimpleStringProperty) helps[1]).getBean());
        }
        resultList2.add(searchValues.ColumnNames[2]);
        List<Object[]> help2 = searchValues.Values;

        for(int i = 0; i < help.size(); i++){
            Object[] helps = help2.get(i);
            resultList2.add((String) ((SimpleStringProperty) helps[2]).getBean());
        }

        myListView.itemsProperty().bind(listProperty);
        myListView2.itemsProperty().bind(listProperty2);






        // Bind the ListView scroll property
        Node n1 = myListView.lookup(".scroll-bar");
        if (n1 instanceof ScrollBar) {
            final ScrollBar bar1 = (ScrollBar) n1;
            Node n2 = myListView2.lookup(".scroll-bar");
            if (n2 instanceof ScrollBar) {
                final ScrollBar bar2 = (ScrollBar) n2;
                bar1.valueProperty().bindBidirectional(bar2.valueProperty());
            }
        }


    }



}
