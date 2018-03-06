package database;

import java.util.List;

public class SearchValues {

    public int ColumnLength;
    public String[] ColumnNames;
    public List<Object[]> Values;

    SearchValues(int columnLength, String[] columnNames, List<Object[]> values) {
        ColumnLength = columnLength;
        ColumnNames = columnNames;
        Values = values;
    }

}
