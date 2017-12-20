package database;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

            ObservableList<Object[]> observableList = FXCollections.observableArrayList();

            while (resultSet.next()){
                Object[] resultObject = new Object[columnLength];

                for(int i = 0; i < columnLength; i++ ){
                    resultObject[i] = new SimpleStringProperty (resultSet.getObject(i+1),columnNames[i]);
                }

                observableList.add(resultObject);

                //Output.add(resultObject);
            }

            return new SearchValues(columnLength, columnNames, observableList);

        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
