package database;

import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MariaDB_Search extends  TempDatabase{

    public static List<Object[]> search(String Question){
        try {
            Statement statement = MariaDB_Connection.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(Question);

            List<Object[]> Output = new ArrayList<>();
            resultSet.next();
            int columnLength = 0;
            try{
                //das <= & +1 zwingt die Methode in die Exception sobald sie durch ist
               for(int i = 1; i <= OverviewColumnMaxLength + 1; i++) {
                   resultSet.getObject(i);
                   columnLength++;
               }
            }catch (SQLDataException e){
                /*String[] ColumnNames = new String[columnLength];
                for(int i = 1; i <= columnLength; i++ ){

                }*/

                Object[] firstLineResult = new Object[columnLength];
                for(int i = 0; i < columnLength; i++ ){
                    firstLineResult[i] = resultSet.getObject(i+1);
                }
                Output.add(firstLineResult);

                while (resultSet.next()){
                    Object[] resultObject = new Object[columnLength];

                    for(int i = 0; i < columnLength; i++ ){
                        resultObject[i] = resultSet.getObject(i+1);
                    }

                    Output.add(resultObject);
                }
                return Output;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
