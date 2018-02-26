package calculations;

import database.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.chart.XYChart;

import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Calculations {

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

    public static List<Attribute> createAttribute() {

        List<String> a = new ArrayList<String>();
        List<Attribute> c = new ArrayList<Attribute>();

        if (TempDatabase.searchValues != null) {
            List<Object[]> values = TempDatabase.searchValues.Values;
            for (int i = 0; i < values.size(); i++) {
                Object[] help = values.get(i);
                if (help[TempDatabase.attributePosition] != null) {
                    String b = ((SimpleStringProperty) help[TempDatabase.attributePosition]).getBean().toString();
                    a = Arrays.asList(b.split("[ ]*,[ ]*"));
                    System.out.println("null" + a.get(0));
                    if (c.isEmpty() == true) {
                        Attribute a1 = new Attribute(a.get(i), 1);
                        System.out.println("apunktgeti" + a.get(i));
                        c.add(0, a1);
                    } else {
                        for (int j = 0; j < c.size(); j++) {
                            if (c.get(j).getName() == a.get(i)) {
                                c.get(j).setCounter(c.get(j).getCounter() + 1);
                            }
                        }
                        Attribute a2 = new Attribute(a.get(i), 1);
                        c.add(c.size(), a2);

                    }
                }

                System.out.println("Couter " + c.get(0).getCounter());
                System.out.println("Name " + c.get(0).getName());
            }

        }

        return c;
    }

    public List<Attribute> splitString() {

        List<String> a = new ArrayList<String>();
        List<Attribute> c = new ArrayList<Attribute>();

        if (TempDatabase.searchValues != null) {
            List<Object[]> values = TempDatabase.searchValues.Values;
            for (int i = 0; i < values.size(); i++) {
                Object[] help = values.get(i);
                if (help[9] != null) {
                    String b = ((SimpleStringProperty) help[TempDatabase.attributePosition]).getBean().toString();
                    a = Arrays.asList(b.split("[ ]*,[ ]*"));
                    for (int j = 0; j < a.size(); j++) {
                        if (c.isEmpty() == true) {
                            Attribute a1 = new Attribute(a.get(j), 1);
                            System.out.println("Stelle 0: " + a.get(i));
                            c.add(0, a1);
                        } else {
                            for (int k = 0; k < c.size(); k++) {
                                if (c.get(k).getName() == a.get(j)) {
                                    c.get(k).setCounter(c.get(k).getCounter() + 1);
                                }
                            }
                        }
                    }
                }

            }



        }

        return c;

    }
}



