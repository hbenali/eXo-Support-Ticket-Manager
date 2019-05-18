/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import dialog.NativeDialog;
import dialog.SendCustMessageDialog;
import dialog.SendReviewRequest;
import entity.MessagesManager;
import entity.TicketManager;
import io.AvatarManager;
import io.NetworkUtils;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hba19
 */
public class ConnectedUsers extends javax.swing.JFrame {

    private DefaultTableModel model;
    private int lscount = 0;
    private String[] usersActivity;

    /**
     * Creates new form ConnectedUsers
     */
    public ConnectedUsers() {
        initComponents();
        this.setIconImage(AvatarManager.getAvatar());
        userstatus.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        checkConnectivity();

    }

    private void checkConnectivity() {
        int delay = 0;   // delay for 5 sec.
        int period = 60000;  // repeat every sec.
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    initTable();
                } catch (Exception ff) {
                }
            }
        }, delay, period);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        userstatus = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        File = new javax.swing.JMenu();
        exit = new javax.swing.JMenuItem();
        Edit = new javax.swing.JMenu();
        AskHelp = new javax.swing.JMenuItem();
        sendmessage = new javax.swing.JMenuItem();
        reviewask = new javax.swing.JMenuItem();
        availableask = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("eXo Support Ticket Manager - Agent Activity");

        userstatus.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Agent", "Status", "Last Activity"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(userstatus);
        if (userstatus.getColumnModel().getColumnCount() > 0) {
            userstatus.getColumnModel().getColumn(1).setPreferredWidth(80);
        }

        File.setText("File");

        exit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/exit.png"))); // NOI18N
        exit.setText("Exit");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        File.add(exit);

        jMenuBar1.add(File);

        Edit.setText("Request");

        AskHelp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        AskHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/helpask.png"))); // NOI18N
        AskHelp.setText("Ask For Help");
        AskHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AskHelpActionPerformed(evt);
            }
        });
        Edit.add(AskHelp);

        sendmessage.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        sendmessage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/messagesend.png"))); // NOI18N
        sendmessage.setText("Send Message");
        sendmessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendmessageActionPerformed(evt);
            }
        });
        Edit.add(sendmessage);

        reviewask.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        reviewask.setIcon(new javax.swing.ImageIcon(getClass().getResource("/reviewask.png"))); // NOI18N
        reviewask.setText("Ask For Review");
        reviewask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reviewaskActionPerformed(evt);
            }
        });
        Edit.add(reviewask);

        availableask.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
        availableask.setIcon(new javax.swing.ImageIcon(getClass().getResource("/availask.png"))); // NOI18N
        availableask.setText("Ask For Availability");
        availableask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                availableaskActionPerformed(evt);
            }
        });
        Edit.add(availableask);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sshask.png"))); // NOI18N
        jMenuItem1.setText("Ask For SSH");
        Edit.add(jMenuItem1);

        jMenuBar1.add(Edit);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 763, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_exitActionPerformed

    private void AskHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AskHelpActionPerformed
        // TODO add your handling code here:
        int pos = userstatus.getSelectedRow();
        if (pos < 0) {
            return;
        }
        String requesterID = this.usersActivity[pos].split("\\|")[0].trim();
        int ans = MessagesManager.send_HELP_REQ(requesterID);
        if (ans == 1) {
            NativeDialog.showInfoMessage(this, "Your helps request has been sent!", "Sent Confirmation");
        } else {
            NativeDialog.showWarningMessage(this, "Could not send your request!", "Sent Error");
        }

    }//GEN-LAST:event_AskHelpActionPerformed

    private void sendmessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendmessageActionPerformed
        // TODO add your handling code here:
        int pos = userstatus.getSelectedRow();
        if (pos < 0) {
            return;
        }
        String requesterID = this.usersActivity[pos].split("\\|")[0];
        SendCustMessageDialog sc = new SendCustMessageDialog(this, requesterID);
        sc.setVisible(true);
    }//GEN-LAST:event_sendmessageActionPerformed

    private void reviewaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reviewaskActionPerformed
        // TODO add your handling code here:
        int pos = userstatus.getSelectedRow();
        if (pos < 0) {
            return;
        }
        String requesterID = this.usersActivity[pos].split("\\|")[0];
        SendReviewRequest srr = new SendReviewRequest(this, requesterID);
        srr.setVisible(true);
    }//GEN-LAST:event_reviewaskActionPerformed

    private void availableaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_availableaskActionPerformed
        // TODO add your handling code here:
        int pos = userstatus.getSelectedRow();
        if (pos < 0) {
            return;
        }
        String requesterID = this.usersActivity[pos].split("\\|")[0].trim();
        int ans = MessagesManager.send_AV_REQ(requesterID);
        if (ans == 1) {
            NativeDialog.showInfoMessage(this, "Your availabity request has been sent!", "Sent Confirmation");
        } else {
            NativeDialog.showWarningMessage(this, "Could not send your request!", "Sent Error");
        }
    }//GEN-LAST:event_availableaskActionPerformed

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
            java.util.logging.Logger.getLogger(ConnectedUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ConnectedUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ConnectedUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConnectedUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ConnectedUsers().setVisible(true);
            }
        });
    }

    private void initTable() {

        usersActivity = NetworkUtils.getUsersActivity(TicketManager.getUserID());
        if (usersActivity == null) {
            return;
        }
        //  String[] jstatus = IO.getJiraStatus(avtickets);
        //String[] jstatus = OnlineTicketManager.getJiraStatus(avtickets);
        //   MyTableModel model =new MyTableModel();
        model = model != null ? model : (DefaultTableModel) userstatus.getModel();
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        lscount = usersActivity != null ? usersActivity.length : lscount;
        
        for (int i = 0; i < usersActivity.length; i++) {
            String tmp[] = usersActivity[i].split("\\|");
            String status;
            if (tmp != null && tmp.length > 2 && tmp[2].equals("1")) {
                status = "Connected";
            } else {
                status = "Not Connected";
            }

            model.addRow(new Object[]{tmp[1], status, tmp[3]});
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem AskHelp;
    private javax.swing.JMenu Edit;
    private javax.swing.JMenu File;
    private javax.swing.JMenuItem availableask;
    private javax.swing.JMenuItem exit;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem reviewask;
    private javax.swing.JMenuItem sendmessage;
    private javax.swing.JTable userstatus;
    // End of variables declaration//GEN-END:variables
}
