package warning;

import javax.swing.*;

public class Warnings {

    public static void warningNoResults(){
        warning("No Results Found", "Error");
    }

    private static void warning(String warningMessage, String title){
        JOptionPane.showMessageDialog(null, warningMessage, title,
                JOptionPane.ERROR_MESSAGE);
    }
}
