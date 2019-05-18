/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dialog;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author hba19
 */
public class NativeDialog {

    public static void showPlainMessage(JFrame j, String message, String title) {
        JOptionPane.showMessageDialog(j, message, title, JOptionPane.PLAIN_MESSAGE);
    }

    public static void showInfoMessage(JFrame j, String message, String title) {
        JOptionPane.showMessageDialog(j, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showWarningMessage(JFrame j, String message, String title) {
        JOptionPane.showMessageDialog(j, message, title, JOptionPane.WARNING_MESSAGE);
    }

    public static void showErrorMessage(JFrame j, String message, String title) {
        JOptionPane.showMessageDialog(j, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static boolean showYESNOQuestion(JFrame j, String message, String title) {
        return JOptionPane.showConfirmDialog(j, message, title, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public static boolean showYESNOCancelQuestion(JFrame j, String message, String title) {
        return JOptionPane.showConfirmDialog(j, message, title, JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION;
    }

    public static boolean showOKCancelQuestion(JFrame j, String message, String title) {
        return JOptionPane.showConfirmDialog(j, message, title, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
    }

}
