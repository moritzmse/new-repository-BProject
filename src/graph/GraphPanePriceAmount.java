package graph;

import database.TempDatabase;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import main.Search;

import java.util.ArrayList;
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
            for(int i = 0; i < values.size(); i++){
                int anz=0;
                Object[] help = values.get(i);
                String price = ((SimpleStringProperty)help[6]).getBean().toString();
                System.out.println("PRICE" + "" + i +": " + price );
                if(!list.contains(price)){
                    for(int j = 0; j < values.size(); j++){
                        Object[] help2 = values.get(j);
                        String price2 = ((SimpleStringProperty)help2[6]).getBean().toString();
                        if(price.equals(price2)){
                            anz++;
                        }
                    }
                    XYChart.Data data = new XYChart.Data(price,anz);
                    series.getData().add(data);
                    list.add(price);
                }
            }
        }
    }

    @FXML
    public void keineAhnung(){
        Search search = new Search();
        search.removeObjectFromPosList();
    }

}
