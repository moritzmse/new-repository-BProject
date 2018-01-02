package database;

import javafx.collections.ObservableList;

import java.util.List;

public class SearchValues {

    public int ColumnLength;
    public String[] ColumnNames;
    //public List<Object[]> Values;
    public ObservableList Values;

    public SearchValues(int columnLength, String[] columnNames, /*List<Object[]>*/ObservableList values) {
        ColumnLength = columnLength;
        ColumnNames = columnNames;
        Values = values;
    }

}
