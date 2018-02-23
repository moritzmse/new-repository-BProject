package database;

import java.util.List;

public class TempDatabase {

    //final static String url = "jdbc:mariadb://192.168.2.105:3306/";
    final static String url = "jdbc:mariadb://sepermarkt.ddns.net:3306/";
    final static String user = "bprojekt";
    final static String password = "Bp2017/18";

    public static SearchValues searchValues;

    public static List ResellerWhere;
    //public static List AttributesWhere;

    //Vars
    public static int manufacturerPosition = 0;
    public static int brandPosition = 1;
    public static int productPosition = 2;
    public static int weekPosition = 4;
    //TODO überprüfen
    public static int pricePosition = 6;
}
