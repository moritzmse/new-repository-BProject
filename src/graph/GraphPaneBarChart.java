package graph;

import database.TempDatabase;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import calculations.Calculations;
import main.Search;

import java.util.ArrayList;
import java.util.List;

public class GraphPaneBarChart {

    public BarChart BarChart;
    public NumberAxis yAxis;
    public CategoryAxis xAxis;

    public void initialize(){
        //TODO reseller anzeigen, X axis in Date, Analysen
        Calculations calc = new Calculations();
        calc.countAttribute();
        BarChart.getData().clear();
        yAxis.setAutoRanging(false);
        yAxis.setTickUnit(1);
        yAxis.setUpperBound(getMaxValue());
        XYChart.Series series = new XYChart.Series();
        if(calc.getGlobalCountAttributes().size() == calc.getGlobalAttributes().size()) {
            for (int i = 0; i < calc.getGlobalAttributes().size(); i++){
                series.getData().add(new XYChart.Data(calc.getGlobalAttributes().get(i), calc.getGlobalCountAttributes().get(i)));
            }
        }
            BarChart.getData().add(series);
        }

        public int getMaxValue(){

            List<Integer> intList = Calculations.getGlobalCountAttributes();
            int counter = 0;
            for(int i = 0; i<intList.size(); i++){
                if(intList.get(i)>counter){
                    counter = intList.get(i);
                }
            }
            return counter;
        }

    @FXML
    public void keineAhnung(){
        Search search = new Search();
        search.removeObjectFromPosList();
    }

    }

