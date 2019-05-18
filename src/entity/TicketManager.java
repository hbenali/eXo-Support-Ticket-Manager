/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author hba19
 */
import gui.Tray;
import entity.Ticket;
import entity.ListTickets;
import io.BasicUtils;
import io.OnlineTicketManager;
import io.TicketsFileManager;
import java.awt.Desktop;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hba19
 */
public class TicketManager {

    private static ListTickets listTickets;
    private static String exoname = null;
    private static String password = null;
    public static boolean isLocal = false;
    private static Tray tray = null;

    private static void init() {
        listTickets = getListTickets();
    }

    public static void exploreDirectory() {
        try {
            // TODO add your handling code here:
            // Desktop.getDesktop().open(IO.getFile().getParentFile());
            Desktop.getDesktop().open(new TicketsFileManager().getDirectory());
        } catch (IOException ex) {
        }
    }

    public static void reset() {
        new TicketsFileManager().setListTickets(new ListTickets(new LinkedList<Ticket>(), exoname), exoname);
    }

    private static void ifNotListTicket() {
        if (listTickets == null) {
            String name = null;

            while (name == null || name.length() < 2 || !BasicUtils.isValidID(name)) {
                name = JOptionPane.showInputDialog(null, "Please input your eXo Account ID:", "eXo ID");
                if (name == null) {
                    System.exit(0);
                }
            }
            UISettings.setSettingValue(SettingType.USER_PASSWORD, "0");
            saveListTickets(new ListTickets(new LinkedList<Ticket>(), name));
        }
    }

    public static ListTickets getListTickets() {
        listTickets = new TicketsFileManager().getListTickets();
        ifNotListTicket();
        fillCred();
        return listTickets;
    }

    public static boolean hasPassword() {
        return password != null;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String passwd) {
        password = passwd;
    }

    public static void saveListTickets(ListTickets ls) {
        listTickets = ls;
        new TicketsFileManager().setListTickets(listTickets, listTickets.getOwner());
        fillCred();
    }

    private static void fillCred() {
        exoname = OnlineTicketManager.getSupportAgentName(listTickets.getOwner());
    }

    public static String getUserName() {
        return listTickets.getOwner().equals(exoname) ? listTickets.getOwner() : BasicUtils.formatName(exoname);
    }

    public static String getUserID() {
        return listTickets.getOwner();
    }

    public static boolean isValidID() {
        String name = listTickets.getOwner();
        if (name == null) {
            return false;
        }
        if (name.contains("\\s") || name.contains(" ") || name.contains("-")) {
            return false;
        }
        return true;
    }

}
