package database;

import javafx.beans.property.SimpleStringProperty;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MariaDB_Search extends  TempDatabase{

    public static SearchValues search(String Question){
        try {
            Statement statement = MariaDB_Connection.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(Question);

            List<Object[]> Output = new ArrayList<>();
            //ObservableList Output = FXCollections.observableArrayList();
            resultSet.next();

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            int columnLength = resultSetMetaData.getColumnCount();
            String[] columnNames = new String[columnLength];

            for(int i = 0; i < columnLength; i++){
                columnNames[i] = resultSetMetaData.getColumnName(i+1);
            }

            //First Line
            Object[] resultObjectFirst = new Object[columnLength];

            //TODO Error:"Current position is after the last row" in der Schleife wenn kein Datensatz existiert
            for(int i = 0; i < columnLength; i++ ){
                resultObjectFirst[i] = new SimpleStringProperty (resultSet.getObject(i+1),columnNames[i]);
            }
            Output.add(resultObjectFirst);

            while (resultSet.next()){
                Object[] resultObject = new Object[columnLength];
                for(int i = 0; i < columnLength; i++ ){
                    resultObject[i] = new SimpleStringProperty (resultSet.getObject(i+1),columnNames[i]);
                }

                //Output.addAll(resultObject);
                Output.add(resultObject);
            }

            return new SearchValues(columnLength, columnNames, Output);

        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
