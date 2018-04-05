package graph;

import database.TempDatabase;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GraphPaneCountWeeks {
    public javafx.scene.chart.BarChart BarChart;
    public ComboBox combobox;

    public void initialize(){
        XYChart.Series series = new XYChart.Series();
        BarChart.getData().clear();
        BarChart.getData().add(series);

        if(TempDatabase.searchValues!=null){
            List<Object[]> values = TempDatabase.searchValues.Values;
            ArrayList resellerList = new ArrayList();
            for(int i = 0; i < values.size(); i++){
                Object[] help = values.get(i);
                String reseller = ((SimpleStringProperty)help[5]).getBean().toString();
                if(!resellerList.contains(reseller)){
                    resellerList.add(reseller);
                }
            }
            combobox.getItems().add("");
            Collections.sort(resellerList, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    int first = Integer.parseInt(o1);
                    int second = Integer.parseInt(o2);
                    return Integer.compare(first,second);
                }
            });
            for(int j = 0; j < resellerList.size(); j++){
                combobox.getItems().add(resellerList.get(j));
            }
        }
    }

    public void comboAction(ActionEvent event){
        String reseller1 = (String) combobox.getValue();

        XYChart.Series series = new XYChart.Series();
        BarChart.getData().clear();
        BarChart.getData().add(series);

        if(TempDatabase.searchValues!=null){
            List<Object[]> values = TempDatabase.searchValues.Values;
            ArrayList weekList = new ArrayList();
            for(int i = 0 ; i < values.size(); i++){
                Object[] help = values.get(i);
                String reseller2 = ((SimpleStringProperty)help[5]).getBean().toString();
                if(reseller1.equals(reseller2)){
                    String date = ((SimpleStringProperty) help[4]).getBean().toString();
                    int week = Integer.parseInt(date.substring(4,6));
                    weekList.add(week);
                }
            }
            ArrayList abstand = new ArrayList();
            ArrayList anz = new ArrayList();
            for(int j = 0 ; j < weekList.size()-1; j++){
                if(weekList.get(j+1)!=null && !abstand.contains((Integer)weekList.get(j+1)- (Integer)weekList.get(j))){
                    abstand.add((Integer)weekList.get(j+1)- (Integer)weekList.get(j));
                    anz.add(1);
                }
                else if(weekList.get(j+1)!=null){
                    int help = (Integer)weekList.get(j+1)- (Integer)weekList.get(j);
                    int index = abstand.indexOf(help);
                    int sum = (int) anz.get(index) + 1;
                    anz.set(index, sum);
                }
            }
            Collections.sort(abstand, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return Integer.compare(o1,o2);
                }
            });
            for(int i = 0 ; i < abstand.size(); i++){
                XYChart.Data data = new XYChart.Data(abstand.get(i)+"", anz.get(i));
                series.getData().add(data);
            }
        }
    }
}

