package gui;

import entity.ListTickets;
import entity.Ticket;
import entity.TicketManager;
import io.GoogleSSManager;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hba19
 */
public class ShowList extends javax.swing.JFrame {

    private ListTickets lst;
    private Main m;
    private TableRowSorter<TableModel> rowSorter;
    private String serverURL;

    /**
     * Creates new form ShowList
     */
    public void initTable() {
        setLocal();
        DefaultTableModel model = (DefaultTableModel) TckList.getModel();
        DateFormat fulldate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String owner = TicketManager.getUserName();
        for (Ticket t : this.lst.getTickets()) {
            model.addRow(new Object[]{t.name, fulldate.format(t.creation), owner, t.isTribe ? "True" : "False", t.time});
        }
    }

    public ShowList() {
        initComponents();
        // test();
        this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/exo.png")).getImage());
    }

    private void test() {
        LinkedList<Ticket> ps = new LinkedList<Ticket>();
        for (int i = 0; i < 20; i++) {

            ps.add(new Ticket("TRIBE-6" + i, "Houssem"));
        }
        ListTickets tmp = new ListTickets(ps, "Houssem");
        lst = tmp;
        initTable();
        initSearch();
        rowSorter = new TableRowSorter<TableModel>(TckList.getModel());
    }

    public ShowList(ListTickets lst, Main m) {
        initComponents();
        ((PlaceholderTextField)this.jtfFilter).setPlaceholder("Type to search...");
        this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/exo.png")).getImage());
        this.lst = lst;
        this.m = m;
        //this.m.setEnabled(false);
        initTable();
        initSearch();
        rowSorter = new TableRowSorter<TableModel>(TckList.getModel());
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (m != null) {
                    m.setEnabled(true);
                }
                e.getWindow().dispose();
            }
        });
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
        TckList = new javax.swing.JTable();
        jtfFilter = new PlaceholderTextField();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        export = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        exit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        find = new javax.swing.JMenuItem();
        copy = new javax.swing.JMenuItem();
        copyall = new javax.swing.JMenuItem();
        delete = new javax.swing.JMenuItem();
        visit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("eXo Support Ticket Manager - Tickets Timesheet");

        TckList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Creation Date", "Owner", "is Tribe", "Hour"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(TckList);

        jtfFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfFilterActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(255, 153, 51));
        jLabel2.setText("Search:");

        jMenu1.setText("File");

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/save.png"))); // NOI18N
        jMenuItem4.setText("Save As");
        jMenuItem4.setEnabled(false);
        jMenu1.add(jMenuItem4);

        export.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        export.setIcon(new javax.swing.ImageIcon(getClass().getResource("/export.png"))); // NOI18N
        export.setText("Google Sheets Export");
        export.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportActionPerformed(evt);
            }
        });
        jMenu1.add(export);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Settings.png"))); // NOI18N
        jMenuItem1.setText("Preferences");
        jMenuItem1.setEnabled(false);
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        exit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/exit.png"))); // NOI18N
        exit.setText("Exit");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        jMenu1.add(exit);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        find.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        find.setIcon(new javax.swing.ImageIcon(getClass().getResource("/find.png"))); // NOI18N
        find.setText("Find");
        find.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findActionPerformed(evt);
            }
        });
        jMenu2.add(find);

        copy.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK));
        copy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/copy.png"))); // NOI18N
        copy.setText("Copy");
        copy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyActionPerformed(evt);
            }
        });
        jMenu2.add(copy);

        copyall.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        copyall.setIcon(new javax.swing.ImageIcon(getClass().getResource("/copyall.png"))); // NOI18N
        copyall.setText("Copy All");
        copyall.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyallActionPerformed(evt);
            }
        });
        jMenu2.add(copyall);

        delete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/delete.png"))); // NOI18N
        delete.setText("Delete");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });
        jMenu2.add(delete);

        visit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        visit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/internet.png"))); // NOI18N
        visit.setText("Visit");
        visit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visitActionPerformed(evt);
            }
        });
        jMenu2.add(visit);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        // TODO add your handling code here:
        this.m.setEnabled(true);
        this.dispose();
    }//GEN-LAST:event_exitActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        // TODO add your handling code here:
        int t[] = TckList.getSelectedRows();
        if (t.length == 0) {
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Are you sure to delete " + t.length + " item" + (t.length > 1 ? "s ?" : " ?"), "Confirm Delete ?", JOptionPane.YES_OPTION)
                == JOptionPane.YES_OPTION) {
            doDelete(t);
            m.updateCount();
        }

    }//GEN-LAST:event_deleteActionPerformed

    private void copyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyActionPerformed
        // TODO add your handling code here:
        copySelected(TckList.getSelectedRows());
    }//GEN-LAST:event_copyActionPerformed

    private void copyallActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyallActionPerformed
        // TODO add your handling code here:
        TckList.selectAll();
        copySelected(TckList.getSelectedRows());

    }//GEN-LAST:event_copyallActionPerformed

    private void findActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findActionPerformed
        // TODO add your handling code here:
        jtfFilter.requestFocus();
    }//GEN-LAST:event_findActionPerformed

    private void jtfFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfFilterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfFilterActionPerformed

    private void visitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visitActionPerformed
        // TODO add your handling code here:
        String url = "https://community.exoplatform.com/portal/support/";
        int t[] = TckList.getSelectedRows();
        for (int pos : t) {
            Ticket currentTicket = lst.getTickets().get(pos);
            try {
                Desktop.getDesktop().browse(new URI(url + currentTicket.name));
            } catch (Exception ex) {
            }
        }

    }//GEN-LAST:event_visitActionPerformed

    private void exportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportActionPerformed
        // TODO add your handling code here:
        String gid = null;
        if (TckList.getSelectedRows().length == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one ticket!", "No Selected Ticket", JOptionPane.WARNING_MESSAGE);
            return;
        }
      //  if (!IO.existsGID()) {
        if (!GoogleSSManager.existsGID()) {
            gid = JOptionPane.showInputDialog(this, "Please input the Google Sheet ID:", "Google Sheet ID Required", JOptionPane.INFORMATION_MESSAGE);
            if (gid != null && gid.trim().length() == 44) {
              //  IO.writeGID(gid);
                GoogleSSManager.writeGID(gid);
                try {
                    Desktop.getDesktop().browse(new URI("https://docs.google.com/spreadsheets/d/" + gid + "/edit?usp=sharing"));
                    JOptionPane.showMessageDialog(this, "Your Sheet ID has been stored! Please enable edit sharing to \n exosupapi@gmail.com", "Allow Edit Permission", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                }
            } else {
                JOptionPane.showMessageDialog(this, "Your Sheet ID is incorrect! ", "ID Invalid", JOptionPane.WARNING_MESSAGE);
            }
        } else {
          //  gid = IO.readGID();
            gid = GoogleSSManager.readGID();
        }

        String paperid = selectPaper(gid);
        if (paperid == null) {
            return;
        }

        int cn = exportTickets(TckList.getSelectedRows(), gid, paperid);
        if (cn > 0) {
            JOptionPane.showMessageDialog(this, "You have exported " + cn + " to your sheet! ", "Insertion successed ! ", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Could not insert tickets!! ", "Export Error", JOptionPane.WARNING_MESSAGE);
        }


    }//GEN-LAST:event_exportActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void setLocal() {
     /*   if (IO.isLocal) {
            this.serverURL = "http://localhost/";
        } else {
            this.serverURL = "https://exosp.alwaysdata.net/";
        }*/
            this.serverURL = "https://exosp.alwaysdata.net/";

    }

    private String selectPaper(String id) {
        String url = this.serverURL;
        //String[][] papers = IO.getPapers(url + "getPapers.php?id=" + id);
        String[][] papers = GoogleSSManager.getPapers( id);
        try {
            if (papers[0].length == 1) {
                return papers[0][0];
            }
            String[] choices = new String[papers[0].length];
            for (int i = 0; i < choices.length; i++) {
                //  System.out.println(papers[1][i]+"--"+papers[0][i]);
                choices[i] = papers[1][i];

            }
            String input = (String) JOptionPane.showInputDialog(null, "Please select a Sheet Paper:",
                    "Sheet Paper Selection", JOptionPane.QUESTION_MESSAGE, null, // Use
                    // default
                    // icon
                    choices, // Array of choices
                    choices[choices != null && choices.length > 0 ? choices.length - 1 : 0]); // Initial choice
            if (input != null) {
                return papers[0][indexOf(input, choices)];
            } else {
                return null;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not connect to the eXo Service", "Service Unavailable", JOptionPane.ERROR_MESSAGE);
            return null;
        }

    }

    public <T> int indexOf(T needle, T[] haystack) {
        for (int i = 0; i < haystack.length; i++) {
            if (haystack[i] != null && haystack[i].equals(needle)
                    || needle == null && haystack[i] == null) {
                return i;
            }
        }

        return -1;
    }

    private int exportTickets(int[] t, String gid, String paperid) {
        String url = this.serverURL;
        DateFormat fulldate = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat monthformat = new SimpleDateFormat("MM");
        DateFormat yearformat = new SimpleDateFormat("yyyy");
        Calendar cal = Calendar.getInstance();
        int nb = 0;
        Date last = null;

        for (int pos : t) {
            try {
                Ticket currentTicket = lst.getTickets().get(pos);
                cal.setTime(currentTicket.creation);
                int weekday = cal.get(Calendar.DAY_OF_WEEK) - 1;
                String purl = url + "addRows.php?id=" + gid + "&hour=" + (currentTicket.time > 0 ? currentTicket.time : 2) + "&name=" + currentTicket.name + "&wkday=" + weekday + "&date=" + fulldate.format(currentTicket.creation);
                if (paperid != null) {
                    purl += "&paperid=" + paperid;
                }

                //int res = IO.getInsertCode(purl, (last != null && isSunday(last, currentTicket.creation)));
                int res = GoogleSSManager.getInsertCode(purl, (last != null && isSunday(last, currentTicket.creation)));

                if (res == 1) {
                    nb++;
                }
                last = currentTicket.creation;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return nb;
    }

    private void copySelected(int[] t) {
        String text = "";
        DateFormat fulldate = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat monthformat = new SimpleDateFormat("MM");
        DateFormat yearformat = new SimpleDateFormat("yyyy");
        Calendar cal = Calendar.getInstance();
        Date last = null;
        for (int pos : t) {
            Ticket currentTicket = lst.getTickets().get(pos);
            cal.setTime(currentTicket.creation);
            String date = fulldate.format(currentTicket.creation);
            int month = Integer.parseInt(monthformat.format(currentTicket.creation));
            int weekday = cal.get(Calendar.DAY_OF_WEEK) - 1;
            weekday = weekday == 0 ? 7 : weekday;
            String typeTicket = yearformat.format(currentTicket.creation) + (currentTicket.isTribe ? "_TN_SERV_SUPO_TRIB_FRNT" : "_TN_SERV_SUPO_" + getTicketPref(currentTicket.name) + "_FRNT");
            if (last != null && isSunday(last, currentTicket.creation)) {
                text += genSunday(last);
            }
            text += date + "	" + month + "	" + weekday + "	2	" + typeTicket + "	" + currentTicket.name.toUpperCase() + "		eXoTN" + "\n";

            last = currentTicket.creation;
        }
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);

    }

    private String genSunday(Date first) {
        DateFormat fulldate = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat monthformat = new SimpleDateFormat("MM");
        DateFormat yearformat = new SimpleDateFormat("yyyy");
        Calendar cal = Calendar.getInstance();
        String text = "";
        cal.setTime(first);
        for (int i = 0; i < 2; i++) {
            cal.add(Calendar.DATE, 1);

            String date = fulldate.format(cal.getTime());
            int month = Integer.parseInt(monthformat.format(cal.getTime()));
            int year = Integer.parseInt(yearformat.format(cal.getTime()));
            int weekday = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (weekday == 0) {
                weekday = 7;
            }
            text += date + "	" + month + "	" + weekday + "	8	" + year + "_TN_ABST_WEND			eXoTN" + "\n";
        }
        return text;
    }

    private boolean isSunday(Date first, Date last) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(first);
        cal2.setTime(last);
        System.out.println("Test: " + ChronoUnit.DAYS.between(cal2.toInstant(), cal1.toInstant()));
        return ChronoUnit.DAYS.between(cal1.toInstant(), cal2.toInstant()) == 3L;
    }

    private String getTicketPref(String s) {
        if (s == null) {
            return null;
        }
        String pl[] = s.split("-");
        if (pl.length > 0 && pl[0].length() > 3) {
            return pl[0].toUpperCase().substring(0, 4);
        } else {
            return pl[0].toUpperCase();
        }
    }

    private void doDelete(int[] t) {
        for (int pos : t) {
            this.lst.getTickets().remove(pos);
            ((DefaultTableModel) TckList.getModel()).removeRow(pos);
          //  IO.saveList(lst);
            TicketManager.saveListTickets(lst);
        }

    }

    private void initSearch() {
        jtfFilter.setForeground(Color.GRAY);
        jtfFilter.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (jtfFilter.getText().equals("Search")) {
                    jtfFilter.setText("");
                    jtfFilter.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (jtfFilter.getText().isEmpty()) {
                    jtfFilter.setForeground(Color.GRAY);
                    jtfFilter.setText("Search");
                }
            }
        });
        new FilterJtable().FilterJtable(TckList, jtfFilter);
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
            java.util.logging.Logger.getLogger(ShowList.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ShowList.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ShowList.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ShowList.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ShowList().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TckList;
    private javax.swing.JMenuItem copy;
    private javax.swing.JMenuItem copyall;
    private javax.swing.JMenuItem delete;
    private javax.swing.JMenuItem exit;
    private javax.swing.JMenuItem export;
    private javax.swing.JMenuItem find;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jtfFilter;
    private javax.swing.JMenuItem visit;
    // End of variables declaration//GEN-END:variables
}
