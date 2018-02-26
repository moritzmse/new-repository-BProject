package calculations;

import database.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.chart.XYChart;

import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Calculations {

   private static List<String> a = new ArrayList<String>();

    public static double calculateMaxPreis() {

        double doubleHelper = 0.00;

        if (TempDatabase.searchValues != null) {
            List<Object[]> values = TempDatabase.searchValues.Values;
            for (int i = 0; i < values.size(); i++) {
                Object[] help = values.get(i);
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

    public static double calculateMinPreis() {

        double doubleHelper = Integer.MAX_VALUE;

        if (TempDatabase.searchValues != null) {
            List<Object[]> values = TempDatabase.searchValues.Values;
            for (int i = 0; i < values.size(); i++) {
                Object[] help = values.get(i);
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

    public static double calculateAvgPreis() {

        double doubleHelper = Integer.MAX_VALUE;
        double sum = 0;
        int counter = 0;


        if (TempDatabase.searchValues != null) {
            List<Object[]> values = TempDatabase.searchValues.Values;
            for (int i = 0; i < values.size(); i++) {
                Object[] help = values.get(i);
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

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public void anzahlAttribute() {

    }

/*    public static List<Attribute> createAttribute() {


        List<Attribute> c = new ArrayList<Attribute>();
        boolean vorhanden = true;

        splitString();

        for (int j = 1; j < a.size(); j++) {    //keine ahnung warum k = 1 und nicht k = 0 lol

            if (c.isEmpty() == true) {
                Attribute a1 = new Attribute(a.get(j), 1);
                System.out.println("Stelle 0: " + a.get(j));
                c.add(0, a1);
            } else {
                for (int k = 0; k < c.size(); k++) {
                    if (c.get(k).getName().equals(a.get(j))) {
                        c.get(k).setCounter(c.get(k).getCounter() + 1);
                        vorhanden = true;

                    } else {vorhanden = false;}
                }
                if(vorhanden==false) {
                    Attribute a2 = new Attribute(a.get(j), 1);
                    //c.add(c.size(), a2);
                    c.add(a2);
                    vorhanden = true;
                }
            }

        }
    return c;
    }

    public static void splitString() {

        List<String> attributString = new ArrayList<String>();
        List<Attribute> c = new ArrayList<Attribute>();
        boolean vorhanden = true;

        if (TempDatabase.searchValues != null) {
            List<Object[]> values = TempDatabase.searchValues.Values;
           for (int i = 0; i < values.size(); i++) {
               Object[] help = values.get(i);
               if (help[9] != null) {
                   String b = ((SimpleStringProperty) help[TempDatabase.attributePosition]).getBean().toString();
                   attributString = Arrays.asList(b.split("[ ]*,[ ]*"));

               }
              a.addAll(attributString);
               System.out.println("a: " + a);
               System.out.println("attributString: " + attributString);
           }

        }
        for(int j = 0; j<a.size(); j++){
            System.out.println(a.get(j));
        }
    }*/


    public static void createAttribute(){

        List<String> attributString = new ArrayList<String>();


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
                        //System.out.println(attributString.get(j));

                        //if(attributes.size() > 0) {

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
                      //  }else{
                         //   attributes.add(a);
                        //    countAttributes.add(1);
                        //}
                    }
                }
            }
        }

        for(int i = 0; i < attributes.size(); i++){

            System.out.println(attributes.get(i) + " : " + countAttributes.get(i));
        }
    }

//Attributanzahlen als Linechart, Balkendiagramm in neuem Tab?
//Angebotshäufigkeit berechnen

}



