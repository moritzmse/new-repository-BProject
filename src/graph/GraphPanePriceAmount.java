package graph;

import database.TempDatabase;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import main.Search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GraphPanePriceAmount {

    public javafx.scene.chart.BarChart BarChart;
    public CategoryAxis xAxis;
    public NumberAxis yAxis;

    public void initialize(){
        XYChart.Series series = new XYChart.Series();
        BarChart.getData().clear();
        BarChart.getData().add(series);

        if(TempDatabase.searchValues!=null){
            List<Object[]> values = TempDatabase.searchValues.Values;
            ArrayList list = new ArrayList();
            List<List> anzPrice = new ArrayList<>();
            for(int i = 0; i < values.size(); i++){
                int anz=0;
                Object[] help = values.get(i);
                String price = ((SimpleStringProperty)help[6]).getBean().toString();
                if(!list.contains(price)){
                    for(int j = 0; j < values.size(); j++){
                        Object[] help2 = values.get(j);
                        String price2 = ((SimpleStringProperty)help2[6]).getBean().toString();
                        if(price.equals(price2)){
                            anz++;
                        }
                    }
                    //---------------------------------
                    List listInner = new ArrayList();
                    String priceFinal = price.replace(",",".");
                    listInner.add(Double.parseDouble(priceFinal));
                    listInner.add(anz);
                    anzPrice.add(listInner);
                    //---------------------------------
                    list.add(price);
                }
            }
            Collections.sort(anzPrice, new Comparator<List>() {
                @Override
                public int compare(List o1, List o2) {
                    double d1 =(double) o1.get(0);
                    double d2 =(double) o2.get(0);
                    return Double.compare(d1,d2);
                }
            });
            for(int i = 0; i < anzPrice.size(); i++){
                String price = anzPrice.get(i).get(0).toString();
                int anz = (int) anzPrice.get(i).get(1);
                XYChart.Data data = new XYChart.Data(price, anz);
                series.getData().add(data);
            }
        }
    }

    @FXML
    public void keineAhnung(){
        Search search = new Search();
        search.removeObjectFromPosList();
    }

}
