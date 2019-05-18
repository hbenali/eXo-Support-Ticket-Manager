/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.util.Date;

/**
 *
 * @author hba19
 */
public class BasicUtils {

    public static String capitalize(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
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

    public static String[] to2DStr(Object[] o) {
        String[] t = new String[o.length];
        for (int i = 0; i < o.length; i++) {
            t[i] = (String) o[i];
        }
        return t;
    }
    public static Date[] to2DDate(Object[] o) {
        Date[] t = new Date[o.length];
        for (int i = 0; i < o.length; i++) {
            t[i] = (Date) o[i];
        }
        return t;
    }
}
