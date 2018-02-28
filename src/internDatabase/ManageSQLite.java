package internDatabase;

import java.sql.*;

public class ManageSQLite {

    private static Connection getConnection(){
        Connection connection = null;
        try{
            Class.forName("org.sqlite.JDBC").newInstance();
            connection = DriverManager.getConnection("jdbc:sqlite::resource:internDatabase/bprojekt.s3db");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static boolean login(String password){
        try{
            ResultSet resultSet = getConnection().createStatement().executeQuery("SELECT password FROM login");
            resultSet.next();
            return password.equals(resultSet.getString(1));
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean setPassword(String oldPassword, String newPassword){
        try{
            if(login(oldPassword)){
                getConnection().createStatement().executeUpdate("UPDATE login SET password = '" + newPassword + "'");
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
