/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dialog;

import entity.MessagesManager;
import io.AvatarManager;
import io.OnlineTicketManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author hba19
 */
public class SendCustMessageDialog extends javax.swing.JFrame {

    private JFrame parent = null;
    private String requester = null;

    /**
     * Creates new form SendCustMessageDialog
     */
    public SendCustMessageDialog() {
        initComponents();
        this.setIconImage(AvatarManager.getAvatar());
        this.getRootPane().setDefaultButton(send);
    }

    public SendCustMessageDialog(JFrame f, String sender) {
        initComponents();
        this.setIconImage(AvatarManager.getAvatar());
        this.getRootPane().setDefaultButton(send);
        parent = f;
        if (parent != null) {
            parent.setEnabled(false);
        }
        requester = sender;
        String name = OnlineTicketManager.getSupportAgentName(requester);
        this.to_user.setText(name);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        to_user = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        content = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        attchedticket = new javax.swing.JTextField();
        attachedjira = new javax.swing.JTextField();
        send = new javax.swing.JButton();
        abort = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Send Message");
        setIconImages(null);
        setUndecorated(true);
        setResizable(false);

        jLabel1.setText("To Agent (*):");

        to_user.setText("Test");
        to_user.setEnabled(false);

        jLabel2.setText("Message (*):");

        content.setColumns(20);
        content.setRows(5);
        jScrollPane1.setViewportView(content);

        jLabel3.setText("Attached Ticket:");

        jLabel4.setText("Attached Jira:");

        attchedticket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attchedticketActionPerformed(evt);
            }
        });

        send.setForeground(new java.awt.Color(0, 204, 102));
        send.setText("Send");
        send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendActionPerformed(evt);
            }
        });

        abort.setForeground(new java.awt.Color(255, 102, 0));
        abort.setText("Abort");
        abort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abortActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(153, 153, 153));
        jLabel5.setText("For multiple tickets, use semilicon ;");

        jLabel6.setForeground(new java.awt.Color(102, 102, 102));
        jLabel6.setText("To send a custom message, please fill the following fields:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel1))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(abort, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(send, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(27, 27, 27)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(attachedjira, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                                        .addComponent(jLabel5)
                                        .addComponent(attchedticket, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addComponent(to_user, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(31, 31, 31))))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(to_user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(attchedticket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(attachedjira, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(send)
                    .addComponent(abort))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void abortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abortActionPerformed
        // TODO add your handling code here:
        if (parent != null) {
            parent.setEnabled(true);
        }
        this.dispose();
    }//GEN-LAST:event_abortActionPerformed

    private void attchedticketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attchedticketActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_attchedticketActionPerformed

    private void sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendActionPerformed
        // TODO add your handling code here:
        this.attachedjira.setText(this.attachedjira.getText().replace("https://jira.exoplatform.org/browse/", ""));
        this.attchedticket.setText(this.attchedticket.getText().replace("https://community.exoplatform.com/portal/support/", ""));

        if (!validateitems()) {
            JOptionPane.showMessageDialog(this, "Please make sure you have correctly filled the message fields", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        System.out.println("OK");
        String ajira = attachedjira.getText().isEmpty() ? null : attachedjira.getText();
        String aticket = attchedticket.getText().isEmpty() ? null : attchedticket.getText();
        int ans = MessagesManager.send_CUST_MSG(requester, content.getText().replace("\n", ";;"), aticket, ajira);
        System.out.println("ans:" + ans);
        if (ans == 1) {
            JOptionPane.showMessageDialog(this, "Your message has been sent!", "Sent Confirmation", JOptionPane.INFORMATION_MESSAGE);
            if (parent != null) {
                parent.setEnabled(true);
            }
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Could not send your message!", "Sent Error", JOptionPane.WARNING_MESSAGE);
        }


    }//GEN-LAST:event_sendActionPerformed

    private boolean validateitems() {
        if (requester == null) {
            return false;
        }
        if (to_user.getText().isEmpty()) {
            return false;
        }
        if (content.getText().isEmpty()) {
            return false;
        }

        return validateticket(attchedticket.getText()) && validateticket(attachedjira.getText());
    }

    private boolean validateitem(String name) {
        return name != null && name.length() > 2 && name.contains("-");
    }

    private boolean validateticket(String name) {
        if (name == null || name.trim().length() == 0) {
            return true;
        }
        if (name.contains("\\;")) {
            String t[] = name.split("\\;");
            for (String item : t) {
                if (!validateitem(item)) {
                    return false;
                }
            }

        } else {
            return validateitem(name);
        }
        return true;
    }

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
            java.util.logging.Logger.getLogger(SendCustMessageDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SendCustMessageDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SendCustMessageDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SendCustMessageDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SendCustMessageDialog().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton abort;
    private javax.swing.JTextField attachedjira;
    private javax.swing.JTextField attchedticket;
    private javax.swing.JTextArea content;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton send;
    private javax.swing.JTextField to_user;
    // End of variables declaration//GEN-END:variables
}
