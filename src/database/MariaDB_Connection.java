package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaDB_Connection {
    public static Connection getConnection(){
        Connection connection = null;
        try{
            Class.forName("org.mariadb.jdbc.Driver");
        }catch ( ClassNotFoundException e ){
            System.out.println("JDBC Error");
        }

        try{
            connection = DriverManager.getConnection(TempDatabase.url+"BP", TempDatabase.user, TempDatabase.password);
        }catch ( SQLException e ){
            System.out.println("(MariaDB_Connection) Database Error");
        }
        return connection;
    }
}

//© Copyright Dennis Hawig