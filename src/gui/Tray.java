package gui;

import entity.TicketManager;
import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.net.MalformedURLException;
import javax.swing.JFrame;

public class Tray {

    private static SystemTray tray;
    private static TrayIcon trayIcon = null;

    public static void init() {
        try {
            tray = SystemTray.getSystemTray();

            //If the icon is a file
            //Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
            //Alternative (if the icon is on the classpath):
             Image image = new javax.swing.ImageIcon(Tray.class.getResource("/exo.png")).getImage();

            trayIcon = new TrayIcon(image, "eXo Available Tickets");
            //Let the system resize the image if needed
            trayIcon.setImageAutoSize(true);
            //Set tooltip text for the tray icon
            trayIcon.setToolTip("eXo Support Ticket Manager");
            tray.add(trayIcon);
        } catch (Exception fx) {
        }
    }

    public static void main(String[] args) throws Exception {
        if (SystemTray.isSupported()) {
            Tray td = new Tray();
            td.displayInfo("Hello");
        } else {
            System.err.println("System tray not supported!");
        }
    }

    public static void displayInfo(String message) {
        //Obtain only one instance of the SystemTray object

        trayIcon.displayMessage(message, "eXo Support Ticket Manager", MessageType.INFO);
    }

    public static void displayWarn(String message) {
        //Obtain only one instance of the SystemTray object

        trayIcon.displayMessage(message, "eXo Support Ticket Manager", MessageType.WARNING);
    }
}
