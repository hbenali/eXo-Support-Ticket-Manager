/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import entity.SettingType;
import entity.UISettings;
import io.NetworkUtils;

/**
 *
 * @author hba19
 */
public class AuthentificationManager {

    public static boolean isOnlineAuthenticated = false;
    private static Main oldMain = null;

    public static void setMain(Main m) {
        oldMain = m;
    }

    public static boolean checkAuthentication() {
        if (!NetworkUtils.isServiceUp()) {
            new Main().setVisible(true);
            return true;
        } else if (UISettings.getSettingValue(SettingType.USER_PASSWORD).equals("0")) {
            new Main().setVisible(true);
            return true;
        } else {
            if (oldMain != null) {
                return askCredinals(oldMain);
            } else {
                return askCredinals();
            }
        }
    }

    private static boolean askCredinals(Main... main) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginAuth auth = new LoginAuth(main);
                    auth.setVisible(true);
                } catch (Exception e) {

                }
            }
        });
        // while(!auth.isAuthorized);
        return true;
    }

    public static void main(String[] args) {
        checkAuthentication();
    }

}
