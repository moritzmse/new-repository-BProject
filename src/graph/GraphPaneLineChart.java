package graph;

import database.TempDatabase;
import javafx.beans.property.SimpleStringProperty;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;


import javafx.scene.control.CheckBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import main.Search;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GraphPaneLineChart {

    public LineChart lineChart;
    public VBox legendVbox;
    public int anzLegend = 0;
    public ArrayList<XYChart.Series> seriesArrayList = new ArrayList<>();

    public void initialize(){
        durchschnitt();
        eachReseller();
        sortSeriesList(seriesArrayList);
        for(XYChart.Series s : seriesArrayList){
            addToLegend(s);
        }
    }

    //Durchschnitt für die einzelnen Wochen
    private void durchschnitt(){
        lineChart.getData().clear();
        XYChart.Series series = new XYChart.Series();
        series.setName("Durchschnitt");
        lineChart.getData().add(series);
        //-----------------------------------------------------
        Node line = series.getNode().lookup(".chart-series-line");
        line.setStyle("-fx-stroke:"+ResellerColor.getColor("0")+";");
        //---------------------------------------------------------
        if(TempDatabase.searchValues != null) {
            List<Object[]> values = TempDatabase.searchValues.Values;
            ArrayList list = new ArrayList();
            for (int i = 0; i < values.size(); i++) {
                int anz = 0;
                double priceFinal = 0;
                double min = 10000;
                double max = 0;
                Object[] help1 = values.get(i);
                String date1 = ((SimpleStringProperty)help1[4]).getBean().toString();
                if(!list.contains(date1)){
                    for (Object[] help2 : values) {
                        String date2 = ((SimpleStringProperty) help2[4]).getBean().toString();
                        String test = ((SimpleStringProperty) help2[6]).getBean().toString();
                        double price = Double.parseDouble(test.replace(",","."));
                        if(date1.equals(date2)){
                            anz++;
                            priceFinal += price;
                            if(price>= max){
                                max = price;
                            }
                            if(price<=min){
                                min = price;
                            }
                        }
                    }
                    priceFinal = priceFinal/anz;
                    int week = Integer.parseInt(date1.substring(4,6));
                    XYChart.Data<Number, Number> data = new XYChart.Data<>(week, priceFinal);
                    series.getData().add(data);
                    //---------------------------------------------------------
                    data.getNode().setStyle("-fx-background-color: "+ResellerColor.getColor("0")+";");
                    //---------------------------------------------------------
                    double finalMax = max;
                    double finalMin = min;
                    data.getNode().setOnMouseEntered(event -> {
                        //Durchschnitts-,Max-,MinPreis sowie Woche werden angezeigt
                        Tooltip tooltip = durchschnittsTooltip(Double.parseDouble(data.getYValue().toString()),finalMax,finalMin,Double.parseDouble(data.getXValue().toString()));;
                        Tooltip.install(data.getNode(),tooltip);
                    });
                    list.add(date1);
                }
            }
        }
        addToLegend(series);
    }

    private void seriesList(XYChart.Series series){
        seriesArrayList.add(series);
    }

    private void sortSeriesList(ArrayList<XYChart.Series> list){
        Collections.sort(list, new Comparator<XYChart.Series>() {
            @Override
            public int compare(XYChart.Series o1, XYChart.Series o2) {
                int i = Integer.parseInt(o1.getName());
                int j = Integer.parseInt(o2.getName());
                return Integer.compare(i,j);
            }
        });
    }

    private void addToLegend(XYChart.Series series) {
        CheckBox checkBox = new CheckBox(series.getName());
        checkBox.setSelected(true);
        if(!checkBox.getText().equals("Durchschnitt")){
            checkBox.setSelected(false);
            series.getNode().setVisible(false);

            //-----------------------------------------------------
            Node line = series.getNode().lookup(".chart-series-line");
            line.setStyle("-fx-stroke: "+ResellerColor.getColor(checkBox.getText())+";");
            //---------------------------------------------------------

            ObservableList<XYChart.Data> s = series.getData();

            checkBox.setTextFill(ResellerColor.getPaintColor(checkBox.getText()));

            for(XYChart.Data d : s){
                d.getNode().setStyle("-fx-background-color: "+ResellerColor.getColor(checkBox.getText())+";");
                d.getNode().setVisible(false);
            }
        }else{
            checkBox.setTextFill(ResellerColor.getPaintColor("0"));
        }
        checkBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(checkBox.isSelected()){
                    series.getNode().setVisible(true);
                    ObservableList<XYChart.Data> s = series.getData();
                    for(XYChart.Data d : s){
                        d.getNode().setVisible(true);
                    }
                }
                else if(!checkBox.isSelected()){
                    series.getNode().setVisible(false);
                    ObservableList<XYChart.Data> s = series.getData();
                    for(XYChart.Data d : s){
                        d.getNode().setVisible(false);
                    }
                }
            }
        });
        if(anzLegend == 0){
            HBox hbox = new HBox();
            legendVbox.getChildren().add(hbox);
            hbox.setSpacing(20);
            hbox.setPadding(new Insets(10, 40, 10, 40));
            hbox.getChildren().add(checkBox);
            anzLegend++;
        }
        else{
            for(Node n : legendVbox.getChildren()){
                if(n instanceof HBox){
                    ((HBox) n).getChildren().add(checkBox);
                }
            }
        }

    }

    //Preise der einzelnen Reseller in den Wochen
    private void eachReseller(){
        if(TempDatabase.searchValues!=null){
            List<Object[]> values = TempDatabase.searchValues.Values;
            ArrayList list = new ArrayList();
            for(int i = 0; i < values.size(); i++){
                Object[] help1 = values.get(i);
                String reseller1 = ((SimpleStringProperty) help1[5]).getBean().toString();
                if(!list.contains(reseller1)){
                    XYChart.Series<Number,Number> series1 = new XYChart.Series();
                    lineChart.getData().add(series1);
                    series1.setName(reseller1);
                    for (Object[] help2 : values) {
                        String reseller2 = ((SimpleStringProperty) help2[5]).getBean().toString();
                        if(reseller1.equals(reseller2)){
                            String date = ((SimpleStringProperty) help2[4]).getBean().toString();
                            String price = ((SimpleStringProperty) help2[6]).getBean().toString();
                            double week = Double.parseDouble(date.substring(4,6));
                            double priceFinal = Double.parseDouble(price.replace(",","."));
                            XYChart.Data<Number, Number> data = new XYChart.Data<>(week, priceFinal);
                            series1.getData().add(data);
                            data.getNode().setOnMouseEntered(event -> {
                                Tooltip tooltip = resellerTooltip(priceFinal,week);
                                Tooltip.install(data.getNode(),tooltip);
                            });
                        }
                    }
                    list.add(reseller1);
                    seriesList(series1);
                }
            }
        }
    }

    private Tooltip durchschnittsTooltip(double average, double max, double min, double week){
        Tooltip tooltip = new Tooltip("Average Price: " + average + "€" + "\n" + "Max Price: " + max + "€" + "\n"
                + "Min Price: " + min + "€" + "\n" + "Week: " + week);
        tooltip.setShowDelay(Duration.millis(0));
        tooltip.setShowDuration(Duration.millis(32767));
        return tooltip;
    }

    private Tooltip resellerTooltip(double price, double week){
        Tooltip tooltip = new Tooltip("Price: " + price + "€" + "\n" + "Week: " + week);
        tooltip.setShowDelay(Duration.millis(0));
        tooltip.setShowDuration(Duration.millis(32767));

        return tooltip;
    }

    @FXML
    public void keineAhnung(){
        Search search = new Search();
        search.removeObjectFromPosList();
        System.out.println("tab aus liste geloescht");
    }
}
