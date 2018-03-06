package database;

import javafx.beans.property.SimpleStringProperty;
import warning.Warnings;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MariaDB_Search extends  TempDatabase{

    public static SearchValues search(String Question){
        try {
            Statement statement = MariaDB_Connection.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(Question);

            List<Object[]> Output = new ArrayList<>();

            resultSet.next();

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            int columnLength = resultSetMetaData.getColumnCount();
            String[] columnNames = new String[columnLength];

            for(int i = 0; i < columnLength; i++){
                columnNames[i] = resultSetMetaData.getColumnName(i+1);
            }

            //First Line
            Object[] resultObjectFirst = new Object[columnLength];

            try {
                for (int i = 0; i < columnLength; i++) {
                    resultObjectFirst[i] = new SimpleStringProperty(resultSet.getObject(i + 1), columnNames[i]);
                }

                Output.add(resultObjectFirst);

                while (resultSet.next()) {
                    Object[] resultObject = new Object[columnLength];
                    for (int i = 0; i < columnLength; i++) {
                        resultObject[i] = new SimpleStringProperty(resultSet.getObject(i + 1), columnNames[i]);
                    }

                    Output.add(resultObject);
                }

            }catch (SQLDataException e){
                Warnings.warningNoResults();
                return null;
            }

            return new SearchValues(columnLength, columnNames, Output);

        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
