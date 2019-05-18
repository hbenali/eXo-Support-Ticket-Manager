/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import entity.SettingType;
import entity.UISettings;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author hba19
 */
public class GoogleSSManager {

    public static boolean existsGID() {
        return readGID() != null && readGID().trim().length() == 44;
    }
    public static void writeGID(String gid) {
        UISettings.setSettingValue(SettingType.GOOGLE_SHEET_ID, gid);
    }

    public static String readGID() {
        return UISettings.getSettingValue(SettingType.GOOGLE_SHEET_ID);
    }

    public static String[][] getPapers(String id) {
        try {
            URL _url = new URL("https://exosp.alwaysdata.net/getPapers.php?id=" +id);
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
}
