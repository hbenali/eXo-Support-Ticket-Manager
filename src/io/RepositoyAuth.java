/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

/**
 *
 * @author Famille
 */
import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class RepositoyAuth extends Authenticator {  
    private static String username = "";
    private static String password = "";

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication (RepositoyAuth.username, 
                RepositoyAuth.password.toCharArray());
    }

    public static void setPasswordAuthentication(String username, String password) {
        RepositoyAuth.username = username;
        RepositoyAuth.password = password;
    }
}
