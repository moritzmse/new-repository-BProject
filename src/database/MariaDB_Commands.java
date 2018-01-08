package database;

public class MariaDB_Commands {

    public static SearchValues normalSearch(String Brand){
        return MariaDB_Search.search("SELECT DISTINCT * FROM OVERVIEW WHERE BRAND like '%"+ Brand +"%'");
    }
}
