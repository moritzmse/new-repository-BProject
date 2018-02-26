package main;

import database.TempDatabase;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Graph {

    @FXML NumberAxis x;
    @FXML NumberAxis y;

    public LineChart lineChart;

    public void generateLineChart(){
       /* NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("uff");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("puuh");*/
       NumberAxis x = new NumberAxis(1,22,0.5);
       NumberAxis y = new NumberAxis(1, 22, 0.5);

        LineChart lineChart = new LineChart(x, y);
        List<Object[]> resultListe;
        resultListe = TempDatabase.searchValues.Values;

        lineChart.getXAxis().setAutoRanging(true);
        lineChart.getYAxis().setAutoRanging(true);

        lineChart.setTitle("Huretochterverlauf");

        Object[] helper = TempDatabase.searchValues.Values.get(0);
        String help = ((SimpleStringProperty) helper[6]).getBean().toString();
        System.out.println("schmodder: " + help);

        Object[] helper1 = TempDatabase.searchValues.Values.get(0);
        String produktName = ((SimpleStringProperty)helper1[TempDatabase.productPosition]).getBean().toString();//Produktname
        System.out.println("bla" + produktName);

        //Muss geändert werden: X-Achse Monate, Y-Achse Preise, ...
        //soll  TempDatabase.searchValues nutzen
        //!!lineChart.getData().clear();
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
//        series.getData().add(new XYChart.Data<String, Number>("Name", 100));
//        series.setName("Werte");
//        lineChart.getData().add(series);

        ObservableList<String>[] helps = new ObservableList[TempDatabase.searchValues.Values.size()];
        series.setName("Testname");
        series.getData().add(new XYChart.Data<Number, Number>(1, 5));
        lineChart.getData().add(series);

        System.out.println(series.getData());


        //for(int i=0; i<TempDatabase.searchValues.Values.size(); i++){
          //  ObservableList<String> items = FXCollections.observableArrayList();
            //!!items.clear();
            //String[] elementList = selectedRowValues.get(i).split(",[ ]*");
            //String string = elementList[2];

            //Number number = NumberFormat.getInstance().parse(elementList[6]);
         //!!   Number number = NumberFormat.getInstance().parse(selectedRowValues.get(TempDatabase.pricePosition)); //Unit Price
            //String date = elementList[4];
         //!!   String date = selectedRowValues.get(TempDatabase.weekPosition);
         //!!   Date d = new SimpleDateFormat("yyyyww").parse(date);
         //!!   String output = new SimpleDateFormat("yyyy-ww").format(d);
            //System.out.println("output = " + output);

         //!!   series.getData().add(new XYChart.Data<String, Number>(output, number));

        //String[] elementList = selectedRowValues.get(0).split(",[ ]*");
        //String productName = elementList[2];
     //!!   String productName = selectedRowValues.get(TempDatabase.productPosition);
     //!!   series.setName(productName/*"Product"*/);

    }


 /*   public void displayTextCalculation(ArrayList<String> selectedRowValues){
        //Durchschnittspreis:
        double helper = 0;
        for(int i=0; i<selectedRowValues.size(); i++){
            String[] elementList = selectedRowValues.get(i).split(",[ ]*");
            String string = elementList[2];
            Double price = Double.parseDouble(elementList[6]);
            helper+=price;
        }
        double avgPrice = helper/selectedRowValues.size();
        textCalculation.setText("Durchschnittspreis für " + *//*string +*//* "Produkt" + ": " + avgPrice);
    } */




}
