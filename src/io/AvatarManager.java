/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import entity.TicketManager;
import java.awt.Image;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author hba19
 */
public class AvatarManager {

    private static Image userAvatar = null;

    private static void init() {

        URL avatar;
        try {
            avatar = new URL("https://community.exoplatform.com/rest/v1/social/users/" + TicketManager.getUserID() + "/avatar");
            userAvatar = ImageIO.read(avatar.openStream());
        } catch (Exception ex) {
            userAvatar = new ImageIcon(AvatarManager.class.getResource("/exo.png")).getImage();
        }
    }
    public static Image getAvatar(){
        if(userAvatar==null)
            init();
        return userAvatar;
    }
    
}
