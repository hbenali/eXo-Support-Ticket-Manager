package gui;

import entity.SettingType;
import entity.Ticket;
import entity.TicketManager;
import entity.UISettings;
import io.AvatarManager;
import io.BasicUtils;
import io.NetworkUtils;
import io.OnlineTicketManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hba19
 */
public class AssignedTickets extends javax.swing.JFrame {

    private final TableCellRenderer defrender;
    private final MyTableCellRenderer mtcr;
    private DefaultTableModel model;
    private Thread lastParticipantThread = null;

    public class MyTableCellRenderer extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {
            Component c = super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus,
                    row, column);
            if (isSelected) {
                c.setForeground(Color.WHITE);
                c.setBackground(Color.ORANGE);
            } else if (((String) table.getValueAt(row, 2)).equals("Closed")) {
                c.setForeground(Color.WHITE);
                c.setBackground(Color.BLACK);
            } else if (((String) table.getValueAt(row, 2)).equals("Resolved - Waiting For Validation")) {
                c.setForeground(Color.WHITE);
                c.setBackground(Color.lightGray);
            } else if (((String) table.getValueAt(row, 2)).equals("Suspended - Waiting For Information")) {
                c.setForeground(Color.WHITE);
                c.setBackground(Color.MAGENTA);
            } else if (((String) table.getValueAt(row, 2)).equals("Resolved - Waiting For Product Fix")) {
                c.setForeground(Color.WHITE);
                c.setBackground(Color.BLUE);
            } else if (((String) table.getValueAt(row, 2)).equals("Resolution Validated")) {
                c.setForeground(Color.WHITE);
                c.setBackground(Color.GREEN);
            } else if (((String) table.getValueAt(row, 2)).equals("Resolution Refused")) {
                c.setForeground(Color.WHITE);
                c.setBackground(Color.RED);
            } else {
                c.setForeground(Color.BLACK);
                c.setBackground(Color.WHITE);
            }
            return c;
        }
    }
    private int lscount = -1;
    private Main m;
    private int lastSelected = -1;

    /**
     * Creates new form AssignedTickets
     */
    public AssignedTickets(Main m) {
        initComponents();
        javtable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        mtcr = new MyTableCellRenderer();
        defrender = javtable.getDefaultRenderer(Object.class);
        if (colormode.isSelected()) {
            javtable.setDefaultRenderer(Object.class, mtcr);
        }
        this.m = m;
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setIconImage(AvatarManager.getAvatar());
        check();
        initSettings();
        initSearch();
        onChangeEvent(scopeCmb);
        onChangeEvent(statusCmb);
        onItemChange(scopeCmb);
        onItemChange(statusCmb);
        javtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        onSelectEvent(javtable);

    }

    private void getLastParticipants(TableModel tm) {
        if (statusCmb.getSelectedIndex() == 0) {
            return;
        }
        if (lastParticipantThread != null && lastParticipantThread.isAlive()) {
            return;
        }
        lastParticipantThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < tm.getRowCount(); i++) {
                    if (!((String) tm.getValueAt(i, 2)).toLowerCase().contains("waiting for information") && !((String) tm.getValueAt(i, 2)).toLowerCase().contains("waiting for validation")) {
                        continue;
                    }

                    String tname = ((String) tm.getValueAt(i, 0)).trim();
                    tm.setValueAt(NetworkUtils.getLastParticipant(tname), i, 8);

                }

            }
        });
        lastParticipantThread.start();

    }

    private void onSelectEvent(JTable j) {
        j.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int sel = j.getSelectedRow();
                lastSelected = sel;
                boolean isNonEmpty = javtable.getValueAt(lastSelected, 6) != null && ((String) javtable.getValueAt(lastSelected, 6)).length() > 0;
                visitjira.setEnabled(lastSelected > -1 && javtable.getModel().getColumnCount() > 5 && isNonEmpty);
            }
        });
    }

    private void onDeSelectEvent(JTable j) {
        j.getSelectionModel().removeListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int sel = j.getSelectedRow();
                lastSelected = -1;
            }
        });
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
        new FilterJtable().FilterJtable(this.javtable, jtfFilter);
    }

    private void onItemChange(JComboBox j) {
        j.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Do Something
                JComboBox it = (JComboBox) e.getSource();
                // System.out.println(it.getSelectedIndex());
                if (it.getItemCount() == 3) {
                    UISettings.setSettingValue(SettingType.TICKET_GROUP_TYPE, it.getSelectedIndex() + "");
                } else {
                    if (lastParticipantThread != null && lastParticipantThread.isAlive()) {
                        lastParticipantThread.stop();
                    }
                    UISettings.setSettingValue(SettingType.TICKET_STATUS_NAME, it.getSelectedIndex() + "");
                }

            }
        });

    }

    private void onChangeEvent(JComboBox j) {
        j.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    loadTck();
                } catch (Exception ex) {
                    Logger.getLogger(AssignedTickets.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void check() {
        int delay = 60000;   // delay for 5 sec.
        int period = 60000;  // repeat every sec.
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    if (lastParticipantThread == null || !lastParticipantThread.isAlive()) {
                        loadTck();
                    }
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
        javtable = new javax.swing.JTable();
        statusCmb = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        scopeCmb = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jtfFilter = new PlaceholderTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        exit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        assignBtn = new javax.swing.JMenuItem();
        visit = new javax.swing.JMenuItem();
        visitjira = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        answer = new javax.swing.JMenuItem();
        statuschanger = new javax.swing.JMenu();
        stopen = new javax.swing.JMenuItem();
        stconsidered = new javax.swing.JMenuItem();
        stinprogress = new javax.swing.JMenuItem();
        stwfi = new javax.swing.JMenuItem();
        stwfv = new javax.swing.JMenuItem();
        stwfpf = new javax.swing.JMenuItem();
        strv = new javax.swing.JMenuItem();
        strr = new javax.swing.JMenuItem();
        stclosed = new javax.swing.JMenuItem();
        streopen = new javax.swing.JMenuItem();
        stprimprov = new javax.swing.JMenuItem();
        thirdpartychanger = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        colormode = new javax.swing.JCheckBoxMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Assigned Tickets");

        javtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Title", "Status", "Created", "Updated", "Creator", "3rd Party", "Attached Jira", "Last Participant"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        javtable.setUpdateSelectionOnSort(false);
        jScrollPane1.setViewportView(javtable);
        if (javtable.getColumnModel().getColumnCount() > 0) {
            javtable.getColumnModel().getColumn(0).setPreferredWidth(18);
            javtable.getColumnModel().getColumn(3).setResizable(false);
            javtable.getColumnModel().getColumn(3).setPreferredWidth(65);
            javtable.getColumnModel().getColumn(4).setResizable(false);
            javtable.getColumnModel().getColumn(4).setPreferredWidth(65);
        }

        statusCmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Open", "Considered", "In Progress", "Suspended - Waiting for information", "Reproduced", "Not Reproduced", "Resolved - Waiting for validation", "Resolved - Wating for Product Fix", "Validation Accepted", "Validation Refused", "Closed", "Reopen", "All Unresolved", "All Resolved", "Jira Only" }));

        jLabel1.setForeground(new java.awt.Color(255, 153, 51));
        jLabel1.setText("Status:");

        scopeCmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Customer", "Tribe" }));

        jLabel2.setForeground(new java.awt.Color(255, 153, 51));
        jLabel2.setText("Customer Type:");

        jLabel4.setForeground(new java.awt.Color(255, 153, 51));
        jLabel4.setText("Search:");

        ((PlaceholderTextField) jtfFilter).setPlaceholder("Type to search...");
        jtfFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfFilterActionPerformed(evt);
            }
        });

        jMenu1.setText("File");

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

        assignBtn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        assignBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assign.png"))); // NOI18N
        assignBtn.setText("Assign");
        assignBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                assignBtnActionPerformed(evt);
            }
        });
        jMenu2.add(assignBtn);

        visit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        visit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/internet.png"))); // NOI18N
        visit.setText("Visit");
        visit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visitActionPerformed(evt);
            }
        });
        jMenu2.add(visit);

        visitjira.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_J, java.awt.event.InputEvent.CTRL_MASK));
        visitjira.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jira.png"))); // NOI18N
        visitjira.setText("Visit Jira");
        visitjira.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visitjiraActionPerformed(evt);
            }
        });
        jMenu2.add(visitjira);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/reset.png"))); // NOI18N
        jMenuItem1.setText("Reload");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        answer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        answer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/answer.png"))); // NOI18N
        answer.setText("Answer");
        answer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                answerActionPerformed(evt);
            }
        });
        jMenu2.add(answer);

        statuschanger.setIcon(new javax.swing.ImageIcon(getClass().getResource("/statuschange.png"))); // NOI18N
        statuschanger.setText("Status");

        stopen.setText("Open");
        statuschanger.add(stopen);

        stconsidered.setText("Considered");
        stconsidered.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stconsideredActionPerformed(evt);
            }
        });
        statuschanger.add(stconsidered);

        stinprogress.setText("In Progress");
        statuschanger.add(stinprogress);

        stwfi.setText("Suspended - Waiting for information");
        statuschanger.add(stwfi);

        stwfv.setText("Resolved - Waiting for validation");
        statuschanger.add(stwfv);

        stwfpf.setText("Resolved - Waiting for product fix");
        statuschanger.add(stwfpf);

        strv.setText("Resolution Validated");
        statuschanger.add(strv);

        strr.setText("Resolution Refused");
        statuschanger.add(strr);

        stclosed.setText("Closed");
        statuschanger.add(stclosed);

        streopen.setText("Reopen");
        streopen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                streopenActionPerformed(evt);
            }
        });
        statuschanger.add(streopen);

        stprimprov.setText("Product Improvement");
        statuschanger.add(stprimprov);

        jMenu2.add(statuschanger);

        thirdpartychanger.setIcon(new javax.swing.ImageIcon(getClass().getResource("/3rdparty.png"))); // NOI18N
        thirdpartychanger.setText("3rd Party");

        jMenuItem2.setText("Empty");
        thirdpartychanger.add(jMenuItem2);

        jMenuItem3.setText("Support");
        thirdpartychanger.add(jMenuItem3);

        jMenuItem4.setText("CST - Customer Sucess Team");
        thirdpartychanger.add(jMenuItem4);

        jMenuItem5.setText("Maintenance");
        thirdpartychanger.add(jMenuItem5);

        jMenuItem6.setText("PM - Product Management");
        thirdpartychanger.add(jMenuItem6);

        jMenuItem7.setText("Engineering");
        thirdpartychanger.add(jMenuItem7);

        jMenuItem8.setText("ITOP - IT Operational Portal");
        thirdpartychanger.add(jMenuItem8);

        jMenuItem9.setText("Customer");
        thirdpartychanger.add(jMenuItem9);

        jMenuItem10.setText("CWI");
        thirdpartychanger.add(jMenuItem10);

        jMenuItem11.setText("Doc - Documentation");
        thirdpartychanger.add(jMenuItem11);

        jMenu2.add(thirdpartychanger);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Display");

        colormode.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        colormode.setSelected(true);
        colormode.setText("Color Mode");
        colormode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/colormode.png"))); // NOI18N
        colormode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colormodeActionPerformed(evt);
            }
        });
        jMenu3.add(colormode);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 582, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(statusCmb, 0, 199, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scopeCmb, 0, 73, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfFilter, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(scopeCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(statusCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel4)))
                .addGap(7, 7, 7))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_exitActionPerformed


    private void visitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visitActionPerformed
        // TODO add your handling code here:
        String url = "https://community.exoplatform.com/portal/support/";
        //  int t[] = javtable.getSelectedRows();
        String name = lastSelected > -1 ? (String) javtable.getValueAt(lastSelected, 0) : null;
        if (name == null) {
            return;
        }
        // for (int pos : t) {
        //String name = (String) javtable.getModel().getValueAt(pos, 0);
        // String name = ticketlist.get(pos);
        try {
            Desktop.getDesktop().browse(new URI(url + name));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // }
    }//GEN-LAST:event_visitActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        try {
            this.loadTck();
        } catch (Exception ef) {
            JOptionPane.showMessageDialog(this, "Could not connect to server ! Please try later", "Service Unavailable", JOptionPane.WARNING_MESSAGE);
            //IO.getTray().displayWarn("Could not connect to server ! Please try later");
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jtfFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfFilterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfFilterActionPerformed

    private void visitjiraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visitjiraActionPerformed
        // TODO add your handling code here:
        String url = "https://jira.exoplatform.org/browse/";
        //int t[] = javtable.getSelectedRows();
        String name = lastSelected > -1 && javtable.getModel().getColumnCount() > 6 ? (String) javtable.getValueAt(lastSelected, 7) : null;
        if (name == null) {
            return;
        }

        try {
            if (name != null && !name.trim().equals("")) {
                if (name.contains("--")) {
                    String t[] = name.split("--");
                    for (int i = 0; i < t.length; i++) {
                        String pname = t[i];
                        Desktop.getDesktop().browse(new URI(url + pname.split("\\s")[0]));
                    }

                } else {
                    Desktop.getDesktop().browse(new URI(url + name.split("\\s")[0]));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_visitjiraActionPerformed

    private void colormodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colormodeActionPerformed
        // TODO add your handling code here:
        UISettings.setSettingValue(SettingType.DISPLAY_COLOR_TICKETS, colormode.isSelected() ? "1" : "0");
        switchColorMode();
    }//GEN-LAST:event_colormodeActionPerformed

    private void answerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_answerActionPerformed
        // TODO add your handling code here:
        String name = lastSelected > -1 ? (String) javtable.getValueAt(lastSelected, 0) : null;
        String body = lastSelected > -1 ? (String) javtable.getValueAt(lastSelected, 1) : null;
        if (name == null || body == null) {
            return;
        }
        body = body.replaceAll("\\s", "%20");
        try {
            if (name != null && !name.trim().equals("")) {
                Desktop.getDesktop().browse(new URI("https://docs.google.com/document/create?title=[" + name + "]%20" + body));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_answerActionPerformed

    private void streopenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_streopenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_streopenActionPerformed

    private void stconsideredActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stconsideredActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stconsideredActionPerformed

    private void assignBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_assignBtnActionPerformed
        // TODO add your handling code here:
        String name = lastSelected > -1 ? (String) javtable.getValueAt(lastSelected, 0) : null;
        if (name == null) {
            return;
        }
        m.ls.addTicket(new Ticket(name, m.ls.getOwner()), true);
        m.updateCount();
        m.controller();
    }//GEN-LAST:event_assignBtnActionPerformed

    private void switchColorMode() {
        if (colormode.isSelected()) {
            javtable.setDefaultRenderer(Object.class, mtcr);
        } else {
            javtable.setDefaultRenderer(Object.class, defrender);
        }
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
            java.util.logging.Logger.getLogger(AssignedTickets.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AssignedTickets.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AssignedTickets.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AssignedTickets.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AssignedTickets(null).setVisible(true);
            }
        });
    }

    private void initSettings() {
        this.statuschanger.setEnabled(AuthentificationManager.isOnlineAuthenticated);
        this.thirdpartychanger.setEnabled(AuthentificationManager.isOnlineAuthenticated);
        this.colormode.setSelected(UISettings.getSettingValue(SettingType.DISPLAY_COLOR_TICKETS).equals("1"));
        switchColorMode();
        this.scopeCmb.setSelectedIndex(Integer.parseInt(UISettings.getSettingValue(SettingType.TICKET_GROUP_TYPE)));
        this.statusCmb.setSelectedIndex(Integer.parseInt(UISettings.getSettingValue(SettingType.TICKET_STATUS_NAME)));
        loadTck();
    }

    public void loadTck() {
        //javtable.getTableHeader().setEnabled(false);
        // String[] avtickets = IO.getAsTickets(statusCmb.getSelectedIndex(), scopeCmb.getSelectedIndex());
        String[] avtickets = OnlineTicketManager.getAsTickets(statusCmb.getSelectedIndex(), scopeCmb.getSelectedIndex());
        if (avtickets == null) {
            return;
        }
        //  String[] jstatus = IO.getJiraStatus(avtickets);
        //String[] jstatus = OnlineTicketManager.getJiraStatus(avtickets);
        //   MyTableModel model =new MyTableModel();
        model = model != null ? model : (DefaultTableModel) javtable.getModel();
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        if (lscount > 0 && avtickets != null && avtickets.length > lscount) {
            if (avtickets.length == 1) {
                String tmp[] = avtickets[0].split("\\|");
                //IO.getTray().displayInfo("New Ticket: [" + tmp[0] + "] has been arrived!");
            }
            //else //JOptionPane.showMessageDialog(this, "New Available Tickets has been arrived!", "New Available Tickets!", JOptionPane.INFORMATION_MESSAGE);
            //{
            //IO.getTray().displayInfo("New Assigned Tickets has been arrived!");
            //}
        }
        lscount = avtickets != null ? avtickets.length : lscount;
        // System.out.println(lscount + "--- Status" + statusCmb.getSelectedItem());

        String isFiltred = statusCmb.getSelectedIndex() > 0 || scopeCmb.getSelectedIndex() > 0 ? " [Filtred]" : "";
        if (lscount > 0) {
            //  this.setTitle(lscount + " Assigned Ticket" + (lscount > 1 ? "s" : "") + isFiltred + " - " + IO.getUserName());
            this.setTitle(lscount + " Assigned Ticket" + (lscount > 1 ? "s" : "") + isFiltred + " - " + TicketManager.getUserName());
        } else {
            this.setTitle("No Assigned Tickets" + isFiltred);
        }
        for (int i = 0; i < avtickets.length; i++) {
            String tmp[] = avtickets[i].split("\\|");
            String creator = tmp != null && tmp.length > 4 ? tmp[4] : "NA";
            String thirdparty = tmp != null && tmp.length > 5 ? tmp[5].replace("EMPTY", "") : "";
            String jira = tmp != null && tmp.length > 6 ? tmp[6] : "";
            String updateDate = tmp != null && tmp.length > 7 ? tmp[7] : "";
            // jira = jira.equals("") ? jira : jira + (jstatus != null && jstatus.length > i && !jstatus[i].equals("") ? " ( " + jstatus[i] + " ) " : "");
            model.addRow(new Object[]{tmp[0], tmp[3].replace("&#39;", "'").replace("&#34;", "\""), BasicUtils.formatName(tmp[1]), tmp[2], updateDate, BasicUtils.formatName(creator), thirdparty, jira});
        }
        getLastParticipants(model);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem answer;
    private javax.swing.JMenuItem assignBtn;
    private javax.swing.JCheckBoxMenuItem colormode;
    private javax.swing.JMenuItem exit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable javtable;
    private javax.swing.JTextField jtfFilter;
    private javax.swing.JComboBox<String> scopeCmb;
    private javax.swing.JComboBox<String> statusCmb;
    private javax.swing.JMenu statuschanger;
    private javax.swing.JMenuItem stclosed;
    private javax.swing.JMenuItem stconsidered;
    private javax.swing.JMenuItem stinprogress;
    private javax.swing.JMenuItem stopen;
    private javax.swing.JMenuItem stprimprov;
    private javax.swing.JMenuItem streopen;
    private javax.swing.JMenuItem strr;
    private javax.swing.JMenuItem strv;
    private javax.swing.JMenuItem stwfi;
    private javax.swing.JMenuItem stwfpf;
    private javax.swing.JMenuItem stwfv;
    private javax.swing.JMenu thirdpartychanger;
    private javax.swing.JMenuItem visit;
    private javax.swing.JMenuItem visitjira;
    // End of variables declaration//GEN-END:variables
}
