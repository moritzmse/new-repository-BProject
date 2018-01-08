package database;

public class MariaDB_Commands {

    public static SearchValues normalSearch(String Brand){
        return MariaDB_Search.search("SELECT DISTINCT * FROM OVERVIEW WHERE BRAND like '%"+ Brand +"%'");
    }

    public static SearchValues resellerSearch(){
        return MariaDB_Search.search("SELECT DISTINCT Reseller FROM BP.OVERVIEW ORDER BY cast(Reseller as int)");
    }
}
