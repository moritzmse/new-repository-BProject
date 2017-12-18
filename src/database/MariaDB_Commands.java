package database;

import java.sql.ResultSet;
import java.util.List;

public class MariaDB_Commands {

    public static List<Object[]> normalSearch(String Brand){
        return MariaDB_Search.search("SELECT DISTINCT * FROM OVERVIEW WHERE BRAND like '%"+ Brand +"%'");
    }
}
