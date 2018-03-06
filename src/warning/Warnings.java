package warning;

import javax.swing.*;

public class Warnings {

    public static void warningNoResults(){
        warning("No Results Found", "Error");
    }

    public static void warningErrorPasswordOld() {
        warning("False old password!", "Error");
    }

    public static void warningErrorPasswordNew() {
        warning("The new passwords don't equals!", "Error");
    }

    public static void warningSuccessfulPassword(){
        successful("Password change was successful", "Checked");
    }

    public static void warngingEmptySearch() {
        warning("Product Search is Empty", "Warning");
    }

    private static void warning(String warningMessage, String title){
        JOptionPane.showMessageDialog(null, warningMessage, title,
                JOptionPane.ERROR_MESSAGE);
    }

    private static void successful(String successMessage, String title){
        JOptionPane.showMessageDialog(null, successMessage, title,
                JOptionPane.INFORMATION_MESSAGE);
    }
}
