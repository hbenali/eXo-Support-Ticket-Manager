/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import entity.Ticket;
import entity.TicketManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author hba19
 */
public class OnlineTicketManager {

    public static String[] getAsTickets(int status, int scope) {
        try {
            String exoid = TicketManager.getUserID();
            ArrayList<String> ar = new ArrayList<String>();
            String scid = exoid != null ? "&id=" + exoid : "";
            URL url = new URL("http://exosp.alwaysdata.net/getAsTickets.php?raw=1&status=" + status + "&scope=" + scope + scid);
            System.out.println("http://exosp.alwaysdata.net/getAsTickets.php?raw=1&status=" + status + "&scope=" + scope + scid);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while (null != (line = br.readLine())) {
                ar.add(line.trim().replace("<br>", ""));
            }
            return BasicUtils.to2DStr(ar.toArray());
        } catch (Exception ex) {
        }
        return null;

    }

    public static String[] getAvTickets() {
        try {
            ArrayList<String> ar = new ArrayList<String>();
            URL url = new URL("http://exosp.alwaysdata.net/getTickets.php?raw");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while (null != (line = br.readLine())) {
                ar.add(line.trim().replace("<br>", ""));
            }
            return BasicUtils.to2DStr(ar.toArray());
        } catch (Exception ex) {
        }
        return null;

    }

    public static int updateUserActivity() {
        try {
            URL url = new URL("http://exosp.alwaysdata.net/updateUserActivity.php?id=" + TicketManager.getUserID());
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while (null != (line = br.readLine())) {
                return Integer.parseInt(line.trim().replace("<br>", ""));
            }
        } catch (Exception ex) {
        }
        return -1;
    }

    public static String getSupportAgentName(String id) {
        try {
            ArrayList<String> ar = new ArrayList<String>();
            URL url = new URL("http://exosp.alwaysdata.net/getSupportAgent.php?id=" + id);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while (null != (line = br.readLine())) {
                return (line.trim().replace("<br>", ""));
            }
        } catch (Exception ex) {
            return id;
        }
        return id;
    }

    public static boolean isValidTicketName(String name) {
        try {
            String tname = name.trim().toUpperCase();
            URL url = new URL("https://community.exoplatform.com/portal/support/" + tname);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            if (null != br.readLine()) {
                return true;
            }
        } catch (Exception ex) {
        }
        return false;

    }

    public static String[] getJiraStatus(String[] tck) {
        String res = "";
        String[] jstatus = new String[tck.length];
        for (int i = 0; i < tck.length; i++) {
            String[] ref = tck[i].split("\\|");
            String tname = ref.length > 6 ? ref[6].replaceAll("&", "").replaceAll("\\s", "") : "";
            res += tname + "|";
        }
        if (res.length() > 2) {
            res = res.substring(0, res.length() - 2).trim();
        }

        System.out.println("Input:" + res);

        try {
            URL url = new URL("http://exosp.alwaysdata.net/getJiraStatus.php?jira=" + res.toUpperCase());
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            if (null != (line = br.readLine())) {
                System.out.println("Output:" + line);
                jstatus = line.split("\\|");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return jstatus;

    }

    public static boolean isValidTicket(Ticket t) {
        try {
            String tname = t.name.trim().toUpperCase();
            URL url = new URL("https://community.exoplatform.com/portal/support/" + tname);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            if (null != br.readLine()) {
                return true;
            }
        } catch (Exception ex) {
        }
        return false;

    }

}
