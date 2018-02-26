package graph;

import database.TempDatabase;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.util.List;

public class GraphPane {

    public LineChart lineChart;

    public void initialize(){
        //TODO reseller anzeigen, X axis in Date, Analysen
        lineChart.getData().clear();
        XYChart.Series series = new XYChart.Series();
        if(TempDatabase.searchValues != null) {
            List<Object[]> values = TempDatabase.searchValues.Values;
            for (int i = 0; i < values.size(); i++) {
                Object[] help = values.get(i);
                if (help[4] != null && help[6] != null) {
                    String date = ((SimpleStringProperty) help[4]).getBean().toString();
                    String price = ((SimpleStringProperty) help[6]).getBean().toString();
                    double week = Double.parseDouble(date.substring(5, 6));
                    double priceFinal = Double.parseDouble(price.replace(",", "."));
                    series.getData().add(new XYChart.Data(week, priceFinal));
                }
            }
            lineChart.getData().add(series);
        }
    }
}
