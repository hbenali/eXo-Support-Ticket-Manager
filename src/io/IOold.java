package io;


import gui.Tray;
import security.JustOneLock;
import entity.Ticket;
import entity.ListTickets;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.security.CodeSource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
public class IOold {

    private static String exoid = null;
    private static String exoname = null;
    private static File f = new File(System.getProperty("user.home") + "/.exotickets/tickets.exo");
    private static File gdoc = new File(System.getProperty("user.home") + "/.exotickets/gdoc.exo");
    private static FileInputStream fileIn;
    public static boolean isLocal = false;
    private static Tray tray = null;

    private static boolean checkDir() {
        File folder = new File(System.getProperty("user.home") + "/.exotickets/");
        if (!folder.exists()) {
            return folder.mkdir();
        }
        return folder.isDirectory();

    }

    public static void lockExc() {
        JustOneLock ua = new JustOneLock();

        if (ua.isAppActive()) {
            JOptionPane.showMessageDialog(null, "Another instance is already running!", "eXo Support Tickets Manager already running", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public static void saveList(ListTickets ls) {
        try {
            if (!checkDir()) {
                System.out.println("Folder not found!");
                return;

            }
            FileOutputStream fileOut = new FileOutputStream(f);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(ls);
            objectOut.close();

        } catch (Exception ex) {
            System.out.println("Error while saving File " + ex.getCause());
        }

    }

    public static ListTickets getList() {
        try {
            if (!checkDir()) {
                System.out.println("Folder not found!");
                return null;
            }
            fileIn = new FileInputStream(f);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            ListTickets res = (ListTickets) objectIn.readObject();
            while (!isValidID(res.getOwner())) {
                setID(res);
            }
            exoid = res.getOwner();
            exoname = getSuppName(exoid);
            return res;
        } catch (Exception ex) {
            System.out.println("Error while reading File " + ex.getMessage());
        }
        return null;
    }

    private static String capitalize(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static String getUserName() {
        return exoid.equals(exoname) ? exoid : formatName(exoname);
    }
    
    public static String getUserID(){
        return exoid;
    }
    
    public static boolean isServiceUp() {
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

    public static String formatName(String name) {
        String res = "";
        String[] nm = name.toLowerCase().split(" ");
        for (int i = 0; i < nm.length; i++) {
            res = res + capitalize(nm[i]) + " ";
        }
        return res.trim();
    }

    public static boolean isValidID(String name) {
        if (name == null) {
            return false;
        }
        if (name.contains("\\s") || name.contains(" ") || name.contains("-")) {
            return false;
        }
        return true;
    }

    public static Tray getTray() {
        return tray != null ? tray : new Tray();
    }

    public static String[] getJiraStatus(String[] tck) {
        String res= "";
        String[] jstatus = new String[tck.length]; 
        for (int i = 0; i < tck.length; i++) {
            String[] ref = tck[i].split("\\|");
            String tname = ref.length>6? ref[6].replaceAll("&","").replaceAll("\\s",""):"";
            res+=tname+"|";
        }
        if(res.length()>2)
          res=res.substring(0, res.length()-2).trim();
        
        System.out.println("Input:"+res);
        
        try {
            URL url = new URL("http://exosp.alwaysdata.net/getJiraStatus.php?jira=" + res.toUpperCase());
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            if (null != (line = br.readLine())) {
                        System.out.println("Output:"+line);
               jstatus = line.split("\\|");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return jstatus;

    }

    public static void init() {
        if (checkDir()) {
            if (!f.exists()) {
                String name = null;
                while (name == null || name.length() < 2 || !isValidID(name)) {
                    name = JOptionPane.showInputDialog(null, "Please input your eXo ID:", "Input ID");
                    if (name == null) {
                        System.exit(0);
                    }
                }
                saveList(new ListTickets(new LinkedList<Ticket>(), name));
                exoid = name;
                exoname = getSuppName(exoid);
            }

        }
        lockExc();
        if (tray == null) {
            tray = new Tray();
        }

    }

    public static void setID(ListTickets lt) {
        if (checkDir()) {
            if (!f.exists()) {
                String name = null;
                while (name == null || name.length() < 2 || !isValidID(name)) {
                    name = JOptionPane.showInputDialog(null, "Please input your eXo ID:", "Input ID");
                    if (name == null) {
                        System.exit(0);
                    }
                }

                lt.setOwner(name);
                exoid = name;
                exoname = getSuppName(exoid);
                saveList(lt);

            }

        }

    }

    public static File getFile() {
        return f;
    }

    public static void reset() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("YY-MM-DD-HH-mm-ss");
        String formattedDate = dateFormat.format(date);
        try {
            fileIn.close();
            Files.move(f.toPath(), new File(System.getProperty("user.home") + "/.exotickets/ticket_" + formattedDate + ".exo").toPath());
            init();
        } catch (IOException ex) {
        }
    }

    public static String readGID() {

        try {
            BufferedReader bf = new BufferedReader(new FileReader(gdoc));
            return bf.readLine().trim();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public static boolean existsGID() {
        return gdoc.exists() && readGID() != null && readGID().trim().length() == 44;
    }

    public static boolean writeGID(String gid) {

        try {
            PrintWriter out = new PrintWriter(new FileWriter(gdoc));
            out.print(gid);
            out.close();
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }

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

    private static String[] to2DStr(Object[] o) {
        String[] t = new String[o.length];
        for (int i = 0; i < o.length; i++) {
            t[i] = (String) o[i];
        }
        return t;
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
            return to2DStr(ar.toArray());
        } catch (Exception ex) {
        }
        return null;

    }

    public static String getSuppName(String id) {
        try {
            ArrayList<String> ar = new ArrayList<String>();
            URL url = new URL("http://exosp.alwaysdata.net/getSupportAgent.php?id=" + id);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while (null != (line = br.readLine())) {
                return (line.trim().replace("<br>", ""));
            }
        } catch (Exception ex) {

        }
        return id;
    }

    public static String[] getAsTickets(int status, int scope) {
        try {
            ArrayList<String> ar = new ArrayList<String>();
            String scid = exoid != null ? "&id=" + exoid : "";
            URL url = new URL("http://exosp.alwaysdata.net/getAsTickets.php?raw=1&status=" + status + "&scope=" + scope + scid);
            System.out.println("http://exosp.alwaysdata.net/getAsTickets.php?raw=1&status=" + status + "&scope=" + scope + scid);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while (null != (line = br.readLine())) {
                ar.add(line.trim().replace("<br>", ""));
            }
            return to2DStr(ar.toArray());
        } catch (Exception ex) {
        }
        return null;

    }

    public static int getInsertCode(String url, boolean... addSunday) {
        try {
            URL _url = new URL(url + (addSunday != null && addSunday.length == 1 && addSunday[0] == true ? "&sun=1" : ""));
            BufferedReader br = new BufferedReader(new InputStreamReader(_url.openStream()));
            String str = "";
            String outp = "";
            while (null != (str = br.readLine())) {
                outp += str;
            }

            Pattern p = Pattern.compile("-?\\d+");
            Matcher m = p.matcher(outp);
            while (m.find()) {
                return Integer.parseInt(m.group());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return -1;
    }

    public static String[][] getPapers(String url) {
        try {
            URL _url = new URL(url);
            BufferedReader br = new BufferedReader(new InputStreamReader(_url.openStream()));
            String str = "";
            String outp = "";
            while (null != (str = br.readLine())) {
                outp += str;
            }
            //  System.out.println(outp);
            outp = outp.replace("[", "").replace("]", "").replace("\"", "");
            // System.out.println(outp);

            if (outp == null || !outp.contains("|")) {
                return null;
            }
            String[] tp = outp.split(",");
            String[][] res = new String[2][tp.length];
            int i = 0;
            for (String elt : tp) {
                String[] part = elt.split("\\|");
                res[0][i] = part[0].trim();
                res[1][i] = part[1].trim();
                i++;
            }
            return res;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static String[] formatAllString(String[] t) {
        if (t == null) {
            return null;
        }
        String[] ft = new String[t.length];
        for (int i = 0; i < ft.length; i++) {
            ft[i] = t[i].trim().toUpperCase();

        }
        return ft;

    }

    public static String[] getColumns(String url) {
        try {
            URL _url = new URL(url);
            BufferedReader br = new BufferedReader(new InputStreamReader(_url.openStream()));
            String str = "";
            String outp = "";
            while (null != (str = br.readLine())) {
                outp += str;
            }
            //  System.out.println(outp);
            outp = outp.replace("[", "").replace("]", "").replace("\"", "");
            // System.out.println(outp);

            if (outp == null || outp.contains("Error")) {
                return null;
            }
            return formatAllString(outp.split(","));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static int getLastUpVersion(String url) {
        try {
            URL _url = new URL(url + "update.php");
            BufferedReader br = new BufferedReader(new InputStreamReader(_url.openStream()));
            String str = "";
            String outp = "";
            while (null != (str = br.readLine())) {
                outp += str;
            }
            return Integer.parseInt(outp);
        } catch (Exception e) {
            return 1000;
        }
    }

    public static void doUpdate(int version) {
        try {
            CodeSource codeSource = IOold.class.getProtectionDomain().getCodeSource();
            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            String currentF = jarFile.getParentFile().getPath();
            // Files.move(new File(currentF).toPath(), new File(currentF.replace(".jar", "_" + (float) version / 1000 + ".jar")).toPath());
            BufferedInputStream in = new BufferedInputStream(new URL("http://exosp.alwaysdata.net").openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(System.getProperty("java.io.tmpdir") + "/eXo_Up.jar");
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                File scr = genWinScript();
                String[] command = {"cmd.exe", "/C", "Start", scr.getAbsolutePath().replace("/", "\\")};
                Process p = Runtime.getRuntime().exec(command);

            } else {
                File scr2 = genLinuxScript();
                scr2.setExecutable(true);
                String[] command = {scr2.getAbsolutePath()};
                Process p = Runtime.getRuntime().exec(command);

            }

            // JOptionPane.showMessageDialog(null, "You have successfully update eXo Support Tickets", "Update Success!", JOptionPane.PLAIN_MESSAGE);
            System.exit(0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Could not update eXo Support Tickets", "Update Fail!", JOptionPane.PLAIN_MESSAGE);
            ex.printStackTrace();
        }

    }

    private static File genWinScript() {
        try {
            CodeSource codeSource = IOold.class.getProtectionDomain().getCodeSource();

            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            String currentF = jarFile.getParentFile().getPath();
            File res = new File(System.getProperty("java.io.tmpdir") + "exoup.bat");
            File nwp = new File(System.getProperty("java.io.tmpdir") + "eXo_Up.jar");
            PrintWriter pw = new PrintWriter(res);
            pw.println("@echo off\n");
            pw.println("cls\n");
            pw.println("echo eXo Suppot Tickets Update\n");
            pw.println("tskill " + Thread.currentThread().getId() + "\n");
            //pw.println("tskill javaw >nul \n");
            pw.println("copy " + nwp.getAbsolutePath().replace("/", "\\") + " " + jarFile.getAbsolutePath().replace("/", "\\") + " >nul \n");
            // pw.println(jarFile.getAbsolutePath().replace("/","\\")+" \n");
            pw.println("start javaw -jar -Xms1024m -Xmx1024m " + jarFile.getAbsolutePath().replace("/", "\\") + "\n");
            pw.println("exit");
            pw.close();
            return res;

        } catch (Exception ex) {
            return null;
        }
    }

    private static File genLinuxScript() {
        try {
            CodeSource codeSource = IOold.class.getProtectionDomain().getCodeSource();

            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            String currentF = jarFile.getParentFile().getPath();
            File res = new File(System.getProperty("java.io.tmpdir") + "/exoup.sh");
            File nwp = new File(System.getProperty("java.io.tmpdir") + "/eXo_Up.jar");
            PrintWriter pw = new PrintWriter(res);
            pw.println("echo eXo Suppot Tickets Update\n");
            pw.println("kill -9 " + Thread.currentThread().getId() + "\n");
            //pw.println("tskill javaw >nul \n");
            pw.println("cp -rf " + nwp.getAbsolutePath() + " " + jarFile.getAbsolutePath() + " >nul \n");
            // pw.println(jarFile.getAbsolutePath().replace("/","\\")+" \n");
            pw.println("java -jar  " + jarFile.getAbsolutePath() + " & \n");
            pw.println("exit");
            pw.close();
            return res;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
