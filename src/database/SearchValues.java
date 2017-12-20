package database;

import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;

public class SearchValues {

    public int ColumnLength;
    public String[] ColumnNames;
    public ObservableList<Object> Values;

    public SearchValues(int columnLength, String[] columnNames, ObservableList<Object> values) {
        ColumnLength = columnLength;
        ColumnNames = columnNames;
        Values = values;
    }

}
