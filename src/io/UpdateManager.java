/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import entity.TicketManager;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.security.CodeSource;
import javax.swing.JOptionPane;

/**
 *
 * @author hba19
 */
public class UpdateManager {

    public static void doUpdate(int version) {
        try {
            CodeSource codeSource = TicketManager.class.getProtectionDomain().getCodeSource();
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
            CodeSource codeSource = TicketManager.class.getProtectionDomain().getCodeSource();

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
            pw.println("start javaw -jar -Xms1024m -Xmx1024m \"" + jarFile.getAbsolutePath().replace("/", "\\") + "\" _autoupdated\n");
            pw.println("exit");
            pw.close();
            return res;

        } catch (Exception ex) {
            return null;
        }
    }

    private static File genLinuxScript() {
        try {
            CodeSource codeSource = TicketManager.class.getProtectionDomain().getCodeSource();

            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            String currentF = jarFile.getParentFile().getPath();
            File res = new File(System.getProperty("java.io.tmpdir") + "/exoup.sh");
            File nwp = new File(System.getProperty("java.io.tmpdir") + "/eXo_Up.jar");
            PrintWriter pw = new PrintWriter(res);
            pw.println("echo eXo Suppot Tickets Update\n");
            pw.println("kill -9 " + Thread.currentThread().getId() + "\n");
            //pw.println("tskill javaw >nul \n");
            pw.println("cp -rf " + nwp.getAbsolutePath() + " " + jarFile.getAbsolutePath().replace(" ", "\\ ") + " >nul \n");
            // pw.println(jarFile.getAbsolutePath().replace("/","\\")+" \n");
            pw.println("java -jar  " + jarFile.getAbsolutePath().replace(" ", "\\ ") + " _autoupdated & \n");
            pw.println("exit");
            pw.close();
            return res;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static int getLastVersion() {
        try {
            URL _url = new URL("https://exosp.alwaysdata.net/update.php");
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

}
