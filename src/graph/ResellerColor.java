package graph;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class ResellerColor {

    private final static String[] colors = {"black","navy", "aqua", "bisque", "blueviolet", "darkcyan", "darkgrey", "darkorange", "darkturquoise", "green", "hotpink", "lime", "maroon", "gold", "tomato"};

    private final static int[][] PaintColors = {
            {0,0,0},//Black
            {0,0,128},//navy
            {0, 255, 255},//Aqua
            {255, 228, 196},//Bisque
            {138, 43, 226},//Blueviolet
            {0,139,139},//Darkcyan
            {169,169,169},//Darkgrey
            {255,140,0},//Darkorange
            {72,209,204},//Darkturquoise
            {0,128,0},//Green
            {255,105,180},//Hotpink
            {0,255,0},//Lime
            {128,0,0},//Maroon
            {255,215,0},//Gold
            {255,69,0}//Tomato
    };

    public static String getColor(String text){
        int i = Integer.parseInt(text);
        return colors[i];
    }

    public static Paint getPaintColor(String text){
        int position = Integer.parseInt(text);
        return Color.rgb(PaintColors[position][0],PaintColors[position][1],PaintColors[position][2]);
    }
}
