/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import dialog.ShowCustMessageDialog;
import dialog.ShowReviewRequest;
import gui.Tray;
import io.NetworkUtils;
import io.OnlineTicketManager;
import java.awt.Desktop;
import java.net.URI;
import javax.swing.JOptionPane;

/**
 *
 * @author hba19
 */
public class MessagesManager {

    public static final String HELP_REQ = "HELP_REQ";
    public static final String HELP_ACC = "HELP_ACC";
    public static final String HELP_REF = "HELP_REF";
    public static final String CUST_MSG = "CUST_MSG";
    public static final String REQ_REV = "REQ_REV";
    public static final String FIN_REV = "FIN_REV";
    public static final String REJ_REV = "REJ_REV";
    public static final String REQ_AV = "REQ_AV";
    public static final String AV_OK = "AV_OK";
    public static final String AV_NO = "AV_NO";
    public static final String SSH_REQ = "SSH_REQ";
    public static final String SSH_OK = "SSH_OK";
    public static final String SSH_NO = "SSH_NO";

    public static void listen() {
        String messages[] = NetworkUtils.getUserMessages(TicketManager.getUserID());
        if (messages == null || messages.length == 0) {
            return;
        }
        for (String item : messages) {
            treat(item);
        }
    }

    private static void treat(String st) {
        System.out.println(st);
        String items[] = st.split("\\|");
        String requesterID = items[0];
        String TYPE = items[2];
        String content = items.length > 3 && items[3] != null && items[3].length() > 0 ? items[3] : null;
        String tickets = items.length > 4 && items[4] != null && items[4].length() > 0 ? items[4] : null;
        String jiras = items.length > 5 && items[5] != null && items[5].length() > 0 ? items[5] : null;
        String requesterName = OnlineTicketManager.getSupportAgentName(requesterID);
        Tray.displayInfo("A new message from " + requesterName + " !");
        if (TYPE.equals(HELP_REQ)) {
            int ans = _show_HELP_REQ(requesterName);
            _send_HELP_ANS(ans, requesterID);
        } else if (TYPE.equals(HELP_ACC)) {
            _show_HELP_ACC(requesterName);
        } else if (TYPE.equals(HELP_REF)) {
            _show_HELP_REF(requesterName);
        } else if (TYPE.equals(CUST_MSG)) {
            _show_CUST_MSG(requesterID, requesterName, content, tickets, jiras);
        } else if (TYPE.equals(REQ_REV)) {
            _show_REQ_REV(requesterID, content, requesterName);
        } else if (TYPE.equals(FIN_REV)) {
            _show_FIN_REV(content, requesterName);
        } else if (TYPE.equals(REJ_REV)) {
            _show_REJ_REV(requesterName);
        } else if (TYPE.equals(REQ_AV)) {
            int ans = _show_AV_REQ(requesterName);
            _send_AV_ANS(ans, requesterID);
        } else if (TYPE.equals(AV_OK)) {
            _show_AV_OK(requesterName);
        } else if (TYPE.equals(AV_NO)) {
            _show_AV_NO(requesterName);
        }
    }

    private static int _show_AV_REQ(String requesterName) {
        return JOptionPane.showConfirmDialog(null, "Are you available ?", requesterName + "'s Question ", JOptionPane.YES_NO_OPTION);
    }

    private static int _show_HELP_REQ(String requesterName) {
        return JOptionPane.showConfirmDialog(null, requesterName + " needs your help ! Could you come please ?", "Help Request", JOptionPane.YES_NO_OPTION);
    }

    private static void _show_REJ_REV(String requesterName) {
        JOptionPane.showMessageDialog(null, "Sorry! I'm busy right now", requesterName + "'s Review Answer", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void _show_HELP_ACC(String requesterName) {
        JOptionPane.showMessageDialog(null, "OK, I'm comming in a sec!", requesterName + "'s Help Answer", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void _show_HELP_REF(String requesterName) {
        JOptionPane.showMessageDialog(null, "Sorry, Can't come!", requesterName + "'s Help Answer", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void _send_HELP_ANS(int answer, String requesterID) {
        if (answer == JOptionPane.YES_OPTION) {
            NetworkUtils.sendUserMessages(TicketManager.getUserID(), requesterID, HELP_ACC, null, null, null);
        } else {
            NetworkUtils.sendUserMessages(TicketManager.getUserID(), requesterID, HELP_REF, null, null, null);
        }
    }

    private static void _send_AV_ANS(int answer, String requesterID) {
        if (answer == JOptionPane.YES_OPTION) {
            NetworkUtils.sendUserMessages(TicketManager.getUserID(), requesterID, AV_OK, null, null, null);
        } else {
            NetworkUtils.sendUserMessages(TicketManager.getUserID(), requesterID, AV_NO, null, null, null);
        }
    }

    public static int send_HELP_REQ(String requesterID) {
        return NetworkUtils.sendUserMessages(TicketManager.getUserID(), requesterID, HELP_REQ, null, null, null);
    }

    public static int send_AV_REQ(String requesterID) {
        return NetworkUtils.sendUserMessages(TicketManager.getUserID(), requesterID, REQ_AV, null, null, null);
    }

    public static int send_CUST_MSG(String requesterID, String content, String aticket, String ajira) {
        return NetworkUtils.sendUserMessages(TicketManager.getUserID(), requesterID, CUST_MSG, content, aticket, ajira);
    }

    public static int send_FIN_REV(String requesterID) {
        return NetworkUtils.sendUserMessages(TicketManager.getUserID(), requesterID, FIN_REV, null, null, null);
    }

    public static int send_REJ_REV(String requesterID) {
        return NetworkUtils.sendUserMessages(TicketManager.getUserID(), requesterID, REJ_REV, null, null, null);
    }

    private static void _show_CUST_MSG(String requesterID, String requesterName, String content, String tickets, String jira) {
        ShowCustMessageDialog smd = new ShowCustMessageDialog(null, requesterID, requesterName, content, tickets, jira);
        smd.setVisible(true);
    }

    public static int send_REQ_REV(String requesterID, String url) {
        return NetworkUtils.sendUserMessages(TicketManager.getUserID(), requesterID, REQ_REV, url, null, null);
    }

    private static void _show_REQ_REV(String requesterID, String url, String requesterName) {
        ShowReviewRequest sr = new ShowReviewRequest(null, requesterID, url, requesterName);
        sr.setVisible(true);
    }

    private static void _show_FIN_REV(String url, String requesterName) {
        JOptionPane.showMessageDialog(null, "Hi! Your answer is ready", requesterName + " has finished the review !", JOptionPane.INFORMATION_MESSAGE);
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception ef) {
        }
    }

    private static void _show_AV_OK(String requesterName) {
        JOptionPane.showMessageDialog(null, "Well, I'm available", requesterName + "'s Availability Answer  !", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void _show_AV_NO(String requesterName) {
        JOptionPane.showMessageDialog(null, "Sorry! I'm busy", requesterName + "'s Availability Answer !", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        listen();
    }

}
