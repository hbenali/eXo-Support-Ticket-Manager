/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hba19
 */
public class SSHOpener {

    public static void runWinCMD() {
        try {
        Process p = Runtime.getRuntime().exec("cmd /c start cmd.exe");
            p.waitFor();
        } catch (Exception ex) {
        }
    }
    public static void main(String[] args) {
        runWinCMD();
    }
}
