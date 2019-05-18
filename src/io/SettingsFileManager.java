/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.util.Base64;
import entity.SettingType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 *
 * @author hba19
 */
public class SettingsFileManager {

    private File _fileStore = new File(System.getProperty("user.home") + "/.exotickets/settings.exo");
    private PrintWriter _pw = null;
    private BufferedReader _br = null;

    private String getSettingID(SettingType st) {
        if (st == SettingType.AUTO_SAVE_MODE) {
            return "AUTO_SAVE_MODE";
        }
        if (st == SettingType.BROWSER_LISTENING) {
            return "BROWSER_LISTENING";
        }
        if (st == SettingType.COLL_MESSAGES) {
            return "COLL_MESSAGES";
        }
        if (st == SettingType.TIMER_MODE) {
            return "TIMER_MODE";
        }
        if (st == SettingType.DISPLAY_COLOR_TICKETS) {
            return "DISPLAY_COLOR_TICKETS";
        }
        if (st == SettingType.PASSIVE_SAVE_MODE) {
            return "PASSIVE_SAVE_MODE";
        }
        if (st == SettingType.USER_PASSWORD) {
            return "USER_PASSWORD";
        }
        if (st == SettingType.GOOGLE_SHEET_ID) {
            return "GOOGLE_SHEET_ID";
        }
        if (st == SettingType.TICKET_GROUP_TYPE) {
            return "TICKET_GROUP_TYPE";
        }
        if (st == SettingType.TICKET_STATUS_NAME) {
            return "TICKET_STATUS_NAME";
        }
        return null;
    }

    private  SettingType getSettingType(String name) {
        if (name.equals("AUTO_SAVE_MODE")) {
            return SettingType.AUTO_SAVE_MODE;
        }
        if (name.equals("BROWSER_LISTENING")) {
            return SettingType.BROWSER_LISTENING;
        }
        if (name.equals("COLL_MESSAGES")) {
            return SettingType.COLL_MESSAGES;
        }
        if (name.equals("TIMER_MODE")) {
            return SettingType.TIMER_MODE;
        }
        if (name.equals("DISPLAY_COLOR_TICKETS")) {
            return SettingType.DISPLAY_COLOR_TICKETS;
        }
        if (name.equals("PASSIVE_SAVE_MODE")) {
            return SettingType.PASSIVE_SAVE_MODE;
        }
        if (name.equals("USER_PASSWORD")) {
            return SettingType.USER_PASSWORD;
        }
        if (name.equals("GOOGLE_SHEET_ID")) {
            return SettingType.GOOGLE_SHEET_ID;
        }
        if (name.equals("TICKET_STATUS_NAME")) {
            return SettingType.TICKET_STATUS_NAME;
        }
        if (name.equals("TICKET_GROUP_TYPE")) {
            return SettingType.TICKET_GROUP_TYPE;
        }
        return null;
    }

    private  String serializeSetting(String key, String value) {
        String item = key + "|" + value;
        return Base64.getEncoder().encodeToString(item.getBytes()) + "--" + item.getBytes().length;

    }

    private  String[] unserializeSetting(String hash) {
        try {
            byte[] by = Base64.getDecoder().decode(hash.split("--")[0]);
            int crc = Integer.parseInt(hash.split("--")[1]);
            if (crc != by.length) {
                System.err.println("CRC Error: " + hash);
                return null;
            }
            String it = new String(by);
            return it.split("\\|");
        } catch (Exception ef) {
            return null;
        }

    }

    private  boolean isSaveFolderExists() {
        return _fileStore.getParentFile().exists();
    }

    private  HashMap<SettingType, String> readSettingsFileSource() {
        HashMap<SettingType, String> _settings = new HashMap<SettingType, String>();
        if (!isSaveFolderExists()) {
            System.err.println("Read Error: " + _fileStore.getParent() + " Not Found!");
            return null;
        }
        try {
            _br = new BufferedReader(new FileReader(_fileStore));
            String line = null;
            while ((line = _br.readLine()) != null) {
                if (!line.startsWith("exosetting[")) {
                    continue;
                }
                String hash = line.replace("exosetting[", "").replace("]", "").trim();
                String elt[] = unserializeSetting(hash);
                if (elt != null && elt.length == 2 && elt[0]!=null) {
                    _settings.put(getSettingType(elt[0]), elt[1]!=null?elt[1]:"-1");
                } 
                    
            }

            _br.close();
            return _settings;
        } catch (Exception e) {
            //e.printStackTrace();
            System.err.println("Error: Cannot read from " + _fileStore.getAbsolutePath() + " !");
            return null;
        }
    }

    public  HashMap<SettingType, String> getSetting() {
        return readSettingsFileSource();
    }

    private  void printItem(SettingType st, HashMap<SettingType, String> hm) {
        if (hm!= null && hm.containsKey(st)) {
            _pw.write("exosetting[" + serializeSetting(getSettingID(st), hm.get(st)) + "]\n");
        } else {
            _pw.write("exosetting[" + serializeSetting(getSettingID(st), "0") + "]\n");
        }
    }

    private  boolean saveSettingsFileStore(HashMap<SettingType, String> hm) {
        if (!isSaveFolderExists()) {
            System.err.println("Write Error: " + _fileStore.getParent() + " Not Found!");
            return false;
        }
        try {
            _pw = _pw != null ? _pw : new PrintWriter(new FileWriter(_fileStore));
            printItem(SettingType.AUTO_SAVE_MODE, hm);
            printItem(SettingType.DISPLAY_COLOR_TICKETS, hm);
            printItem(SettingType.PASSIVE_SAVE_MODE, hm);
            printItem(SettingType.USER_PASSWORD, hm);
            printItem(SettingType.GOOGLE_SHEET_ID, hm);
            printItem(SettingType.TIMER_MODE, hm);
            printItem(SettingType.BROWSER_LISTENING, hm);
            printItem(SettingType.TICKET_GROUP_TYPE, hm);
            printItem(SettingType.TICKET_STATUS_NAME, hm);
            printItem(SettingType.COLL_MESSAGES, hm);
            _pw.close();
            return true;
        } catch (Exception e) {
            System.err.println("Error: Cannot save to " + _fileStore.getAbsolutePath() + " !");
            return false;
        }

    }
    public  void setSettings(HashMap<SettingType, String> settings){
        saveSettingsFileStore(settings);
    }
}
