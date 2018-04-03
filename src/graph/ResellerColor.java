package graph;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class ResellerColor {

    private final static String[] colors = {"blue", "aqua", "bisque", "blueviolet", "darkcyan", "darkgrey", "darkorange", "darkturquoise", "green", "hotpink", "lime", "maroon", "gold", "tomato"};

    public static String getColor(String text){
        int i = Integer.parseInt(text);
        return colors[i-1];
    }

    public static Paint getPaintColor(String text){
        return Color.rgb(127,233,110);
    }
}
