package internDatabase;

import java.sql.*;

public class ManageSQLite {

    private static Connection getConnection(){
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite::resource:internDatabase/bprojekt.s3db");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static boolean login(String password){
        try{
            Connection connection = getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT password FROM login");
            resultSet.next();
            boolean output = password.equals(resultSet.getString(1));
            connection.close();
            return output;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean setPassword(String oldPassword, String newPassword){
        try{
            if(login(oldPassword)){
                Connection connection = getConnection();
                Statement statement = connection.createStatement();
                statement.executeUpdate("UPDATE login SET password = '" + newPassword + "'");
                connection.close();
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
