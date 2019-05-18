/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import io.AutocompleteJComboBox;
import io.AvatarManager;
import io.GetStandalone;
import io.StringSearchable;
import io.TicketsFileManager;
import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.swing.JOptionPane;

/**
 *
 * @author hba19
 */
public class GeteXoPlatform extends javax.swing.JFrame {

    ArrayList<String> plflist;
    GetStandalone gs = new GetStandalone();
    private Thread downThread;

    /**
     * Creates new form GeteXoPlatform
     */
    public GeteXoPlatform() {
        initComponents();
        closeEvent();
        plfselector.requestFocus();
        plflist = gs.getPLFList(!servertoggle.isSelected());
        this.setIconImage(AvatarManager.getAvatar());
        this.downloadBtn.setVisible(false);
        this.getRootPane().setDefaultButton(downloadBtn);
        this.downloadProgress.setValue(0);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        servertoggle = new javax.swing.JToggleButton();
        downloadProgress = new javax.swing.JProgressBar();
        plfselector = new PlaceholderTextField();
        downloadBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("eXo Support Ticket Manager - PLF Downloader");
        setResizable(false);

        servertoggle.setForeground(new java.awt.Color(0, 255, 0));
        servertoggle.setText("Tomcat");
        servertoggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                servertoggleActionPerformed(evt);
            }
        });

        downloadProgress.setStringPainted(true);
        downloadProgress.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                downloadProgressMouseClicked(evt);
            }
        });

        ((PlaceholderTextField) plfselector).setPlaceholder("Type PLF Version...");

        downloadBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(servertoggle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(downloadBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(plfselector, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(downloadProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(downloadProgress, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(servertoggle)
                .addComponent(plfselector)
                .addComponent(downloadBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void servertoggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_servertoggleActionPerformed
        // TODO add your handling code here:
        if (servertoggle.isSelected()) {
            servertoggle.setText("JBoss");
            servertoggle.setForeground(Color.CYAN);
        } else {
            servertoggle.setText("Tomcat");
            servertoggle.setForeground(Color.GREEN);

        }
        plflist = gs.getPLFList(!servertoggle.isSelected());

    }//GEN-LAST:event_servertoggleActionPerformed

    private void closeEvent() {
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                if (downThread != null && downThread.isAlive()) {
                    if (JOptionPane.showConfirmDialog(null, "Would you like to continue downloading ?", "Continue Download", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
                        downThread.stop();
                        downloadProgress.setValue(0);
                        plfselector.setText("");
                        plfselector.setEnabled(true);
                    }
                }

                // your code
            }
        });
    }

    private void downloadBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadBtnActionPerformed
        // TODO add your handling code here:
        if (plflist == null || plflist.size() == 0) {
            return;
        }
        if (plfselector.getText().trim().length() == 0) {
            return;
        }
        if (!plflist.contains(plfselector.getText())) {
            JOptionPane.showMessageDialog(this, plfselector.getText() + " is not found!", "Wrong PLF Name!", JOptionPane.WARNING_MESSAGE);
        } else {
            if (downThread != null && downThread.isAlive()) {
                return;
            }
            this.plfselector.setEnabled(false);
            downThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    File result = gs.downloadPLF(plfselector.getText(), !servertoggle.isSelected(), downloadProgress);
                    plfselector.setEnabled(true);
                    if (result == null) {
                        JOptionPane.showMessageDialog(null, "Could not download eXo Platform!", "Download Fail!", JOptionPane.ERROR_MESSAGE);
                    } else {
                        try {
                            Desktop.getDesktop().open(result.getParentFile());
                        } catch (IOException ex) {
                        }
                    }
                }
            });
            downThread.start();
        }

    }//GEN-LAST:event_downloadBtnActionPerformed

    private void downloadProgressMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_downloadProgressMouseClicked
        // TODO add your handling code here:
        if (downThread != null && downThread.isAlive()) {
            if (JOptionPane.showConfirmDialog(null, "Would you like to stop downloading ?", "Stop Download", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                downThread.stop();
                downloadProgress.setValue(0);
                plfselector.setEnabled(true);
            }
        }
    }//GEN-LAST:event_downloadProgressMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GeteXoPlatform.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GeteXoPlatform.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GeteXoPlatform.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GeteXoPlatform.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GeteXoPlatform().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton downloadBtn;
    private javax.swing.JProgressBar downloadProgress;
    private javax.swing.JTextField plfselector;
    private javax.swing.JToggleButton servertoggle;
    // End of variables declaration//GEN-END:variables
}