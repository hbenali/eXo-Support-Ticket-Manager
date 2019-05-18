/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import io.SettingsFileManager;
import java.util.HashMap;

/**
 *
 * @author hba19
 */
public class UISettings {

    private static HashMap<SettingType, String> settings = null;

    private static HashMap<SettingType, String> getDefaultSetting() {
        HashMap<SettingType, String> _settings = new HashMap<SettingType, String>();
        _settings.put(SettingType.USER_PASSWORD, "-1");
        _settings.put(SettingType.AUTO_SAVE_MODE, "0");
        _settings.put(SettingType.PASSIVE_SAVE_MODE, "0");
        _settings.put(SettingType.TIMER_MODE, "0");
        _settings.put(SettingType.DISPLAY_COLOR_TICKETS, "1");
        _settings.put(SettingType.GOOGLE_SHEET_ID, "-1");
        _settings.put(SettingType.BROWSER_LISTENING, "-1");
        _settings.put(SettingType.TICKET_GROUP_TYPE, "0");
        _settings.put(SettingType.TICKET_STATUS_NAME, "0");
        _settings.put(SettingType.COLL_MESSAGES, "1");
        return _settings;
    }

    public static void reset() {
        settings = getDefaultSetting();
        new SettingsFileManager().setSettings(settings);
    }

    private static void checkIntegrity() {
        if (!settings.containsKey(SettingType.AUTO_SAVE_MODE)) {
            settings.put(SettingType.AUTO_SAVE_MODE, "0");
        }
        /**
         * ***************************************************
         */
        if (!settings.containsKey(SettingType.BROWSER_LISTENING)) {
            settings.put(SettingType.BROWSER_LISTENING, "0");
        }
        /**
         * ***************************************************
         */
        if (!settings.containsKey(SettingType.DISPLAY_COLOR_TICKETS)) {
            settings.put(SettingType.DISPLAY_COLOR_TICKETS, "0");
        }
        /**
         * ***************************************************
         */
        if (!settings.containsKey(SettingType.GOOGLE_SHEET_ID)) {
            settings.put(SettingType.GOOGLE_SHEET_ID, "-1");
        }
        /**
         * ***************************************************
         */
        if (!settings.containsKey(SettingType.PASSIVE_SAVE_MODE)) {
            settings.put(SettingType.PASSIVE_SAVE_MODE, "0");
        }
        /**
         * ***************************************************
         */
        if (!settings.containsKey(SettingType.TIMER_MODE)) {
            settings.put(SettingType.TIMER_MODE, "0");
        }
        /**
         * ***************************************************
         */
        if (!settings.containsKey(SettingType.USER_PASSWORD)) {
            settings.put(SettingType.USER_PASSWORD, "0");
        }
        /**
         * ***************************************************
         */
        if (!settings.containsKey(SettingType.TICKET_GROUP_TYPE)) {
            settings.put(SettingType.TICKET_GROUP_TYPE, "0");
        }
        /**
         * ***************************************************
         */
        if (!settings.containsKey(SettingType.TICKET_STATUS_NAME)) {
            settings.put(SettingType.TICKET_STATUS_NAME, "0");
        }
        /**
         * ***************************************************
         */
        if (!settings.containsKey(SettingType.COLL_MESSAGES)) {
            settings.put(SettingType.COLL_MESSAGES, "0");
        }
        /**
         * ***************************************************
         */
        new SettingsFileManager().setSettings(settings);

    }

    private static void init() {
        settings = new SettingsFileManager().getSetting();
        if (settings == null) {
            settings = getDefaultSetting();
            new SettingsFileManager().setSettings(settings);
        }

        //  checkIntegrity();
    }

    public static void setSettingValue(SettingType st, String value) {
        if (settings == null) {
            init();
        }
        settings.put(st, value);
        new SettingsFileManager().setSettings(settings);
    }

    public static String getSettingValue(SettingType st) {
        if (settings == null) {
            init();
        }
        if (settings.containsKey(st)) {
            return settings.get(st);
        } else {
            return "0";
        }
    }

    public static void main(String[] args) {
        setSettingValue(SettingType.TIMER_MODE, "30");
        System.out.println(getSettingValue(SettingType.USER_PASSWORD));
        System.out.println(getSettingValue(SettingType.AUTO_SAVE_MODE));
        System.out.println(getSettingValue(SettingType.DISPLAY_COLOR_TICKETS));
        System.out.println(getSettingValue(SettingType.PASSIVE_SAVE_MODE));
        System.out.println(getSettingValue(SettingType.GOOGLE_SHEET_ID));
        System.out.println(getSettingValue(SettingType.BROWSER_LISTENING));
        System.out.println(getSettingValue(SettingType.TICKET_GROUP_TYPE));
        System.out.println(getSettingValue(SettingType.TICKET_STATUS_NAME));
        System.out.println(getSettingValue(SettingType.COLL_MESSAGES));
    }

}
