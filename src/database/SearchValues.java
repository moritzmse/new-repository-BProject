package database;

import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;

public class SearchValues {

    public int ColumnLength;
    public String[] ColumnNames;
    public ObservableList<HashMap> Values;

    public SearchValues(int columnLength, String[] columnNames, ObservableList<HashMap> values) {
        ColumnLength = columnLength;
        ColumnNames = columnNames;
        Values = values;
    }

}
