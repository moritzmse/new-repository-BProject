package calculations;

import database.*;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Calculations {

    private static String globalProductName = "";
    private static List<String> globalAttributes = new ArrayList<>();
    private static List<Integer> globalCountAttributes = new ArrayList<>();


    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static void countAttribute(){

        List<String> attributString;

        List<String> attributes = new ArrayList<>();
        List<Integer> countAttributes = new ArrayList<>();

        if(TempDatabase.searchValues != null){
            List<Object[]> values = TempDatabase.searchValues.Values;
            for(int i = 0; i < TempDatabase.searchValues.Values.size(); i++){
                Object[] help = values.get(i);

                if (help[9] != null) {
                    String b = ((SimpleStringProperty) help[TempDatabase.attributePosition]).getBean().toString();
                    attributString = Arrays.asList(b.split("[ ]*,[ ]*"));

                    //die 1 muss dahin, weil sonst die leerstellen vorne weg auch gezählt werden
                    for(int j = 1; j < attributString.size(); j++) {
                        String a = attributString.get(j);

                        boolean helper = true;

                        for (int k = 0; k < attributes.size(); k++) {
                            if (attributes.get(k).equals(a)) {
                                countAttributes.set(k, countAttributes.get(k) + 1);
                                helper = false;
                                break;
                            }
                        }

                        if (helper) {
                            attributes.add(a);
                            countAttributes.add(1);
                        }
                    }
                }
            }
        }

        globalAttributes = attributes;
        globalCountAttributes = countAttributes;

        for(int i = 0; i < attributes.size(); i++){

            System.out.println(attributes.get(i) + " : " + countAttributes.get(i));
        }
    }

    public static List<String> getGlobalAttributes() {
        return globalAttributes;
    }

    public static List<Integer> getGlobalCountAttributes() {
        return globalCountAttributes;
    }

//Attributanzahlen als Linechart, Balkendiagramm in neuem Tab?
//Angebotshäufigkeit berechnen

    public static String getProductName(){

        if(TempDatabase.searchValues.Values != null) {
            List<Object[]> values = TempDatabase.searchValues.Values;
            Object[] help = values.get(0);
            String productName = ((SimpleStringProperty) help[TempDatabase.productPosition]).getBean().toString();

            String[] parts = productName.split("[ ]");
            globalProductName = parts[0];
        }
        return globalProductName;
    }

    public static double calculateMaxPreis(SearchValues searchValues) {

        double doubleHelper = 0.00;

        if (searchValues != null) {
            List<Object[]> values = searchValues.Values;
            for (Object[] help : values) {
                if (help[TempDatabase.pricePosition] != null) {
                    String price = ((SimpleStringProperty) help[TempDatabase.pricePosition]).getBean().toString();
                    double priceFinal = Double.parseDouble(price.replace(",", "."));
                    if (priceFinal > doubleHelper) {
                        doubleHelper = priceFinal;
                    }
                }
            }
        }
        return doubleHelper;
    }

    public static double calculateMinPreis(SearchValues searchValues) {

        double doubleHelper = Integer.MAX_VALUE;

        if (searchValues != null) {
            List<Object[]> values = searchValues.Values;
            for (Object[] help : values) {
                if (help[TempDatabase.pricePosition] != null) {
                    String price = ((SimpleStringProperty) help[TempDatabase.pricePosition]).getBean().toString();
                    double priceFinal = Double.parseDouble(price.replace(",", "."));

                    if (priceFinal < doubleHelper) {
                        doubleHelper = priceFinal;
                    }

                }
            }

        }
        return doubleHelper;
    }

    public static double calculateAvgPreis(SearchValues searchValues) {

        double doubleHelper;
        double sum = 0;
        int counter = 0;

        if (searchValues != null) {
            List<Object[]> values = searchValues.Values;
            for (Object[] help : values) {
                if (help[TempDatabase.pricePosition] != null) {
                    String price = ((SimpleStringProperty) help[TempDatabase.pricePosition]).getBean().toString();
                    double priceFinal = Double.parseDouble(price.replace(",", "."));

                    sum = sum + priceFinal;
                    counter = counter + 1;
                }
            }
        }

        doubleHelper = sum / counter;
        return round(doubleHelper, 4);
    }
}
