package gui;

import entity.ListTickets;
import entity.MessagesManager;
import entity.SettingType;
import entity.Ticket;
import entity.TicketManager;
import entity.UISettings;
import io.AvatarManager;
import io.NetworkUtils;
import io.OnlineTicketManager;
import io.TicketHttpServer;
import io.UpdateManager;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.LinkedList;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import security.JustOneLock;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hba19
 */
public class Main extends javax.swing.JFrame {

    public ListTickets ls;
    String[] oldAvailable = new String[0];
    public boolean recieveMessages = false;
    public final static int VERSION = 4100;
    private Timer timer;
    private long counterTimer = 0;
    private int lastTicketIndex = -1;
    private Thread animator;
    private AssignedTickets as;
    private AvailableTickets av;
    private static Tray tray;
    public Main me;
    final TicketHttpServer tp = new TicketHttpServer();

    public static void init() {

        //  lockExc();
        if (tray == null) {
            tray = new Tray();
        }

    }
    private Thread tbrowser;
    private Clipboard SYSTEM_CLIPBOARD;
    private LoginAuth auth;
    private GeteXoPlatform gexo;
    private ConnectedUsers cu;

    private void initSettings() {
        this.passive.setSelected(UISettings.getSettingValue(SettingType.PASSIVE_SAVE_MODE).equals("1"));
        this.automode.setSelected(UISettings.getSettingValue(SettingType.AUTO_SAVE_MODE).equals("1"));
        switchAutoMode();
        this.counter.setSelected(UISettings.getSettingValue(SettingType.TIMER_MODE).equals("1"));
        switchTimerMode();
        this.browserl.setSelected(UISettings.getSettingValue(SettingType.BROWSER_LISTENING).equals("1"));
        browserListener();
        this.passask.setText("Switch " + (UISettings.getSettingValue(SettingType.USER_PASSWORD).equals("0") ? "Online" : "Offline") + " Mode");
        this.mesagesswitch.setText("Turn " + (UISettings.getSettingValue(SettingType.COLL_MESSAGES).equals("0") ? "on" : "off") + " Messages");
        this.recieveMessages = UISettings.getSettingValue(SettingType.COLL_MESSAGES).equals("1");
    }

    private void checkAvailableTickets() {
        int delay = 35000;   // delay for 5 sec.
        int period = 60000;  // repeat every sec.
        java.util.Timer timer = new java.util.Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    String[] avtickets = OnlineTicketManager.getAvTickets();
                    if (avtickets.length > oldAvailable.length) {
                        if (oldAvailable != null && oldAvailable.length == 1) {
                            String tmp[] = oldAvailable[0].split("\\|");
                            Tray.displayInfo("New Ticket: [" + tmp[0] + "] has been arrived!");
                        } else {
                            Tray.displayInfo("New Open Tickets !");
                        }
                        oldAvailable = avtickets;
                    }

                } catch (Exception ff) {
                }
            }
        }, delay, period);

    }

    public void lockExc() {
        JustOneLock ua = new JustOneLock();

        if (ua.isAppActive()) {
            JOptionPane.showMessageDialog(null, "Another instance is already running!", "eXo Support Tickets Manager already running", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void updateConnectivity() {
        int delay = 0;   // delay for 5 sec.
        int period = 60000;  // repeat every sec.
        java.util.Timer timer = new java.util.Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    OnlineTicketManager.updateUserActivity();
                } catch (Exception ff) {
                }
            }
        }, delay, period);

    }

    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        reset.setVisible(false); // Need 2 be fixed !
        me = this;
        lockExc();
        this.addTicket.setVisible(false);
        ((PlaceholderTextField) ticketname).setPlaceholder("Please input the ticket name");
        checkforUpdate();
        Tray.init();
        initSettings();
        ClipoardListner();
        //  IO.init();
        this.getRootPane().setDefaultButton(addTicket);
        //ls = IO.getList();
        ls = TicketManager.getListTickets();
        // if(ls==null) ls = new ListTickets(new LinkedList<Ticket>(), "");
        this.tcount.setText(ls.getTickets().size() + "");
        //   this.setTitle("eXo Support Ticket Manager - " + IO.getUserName());
        this.setTitle("eXo Support Ticket Manager - " + TicketManager.getUserName());
        controller();
        checkMessages();
        updateConnectivity();
        checkAvailableTickets();
        this.exo.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    try {
                        Desktop.getDesktop().browse(new URI("http://www.exoplatform.com"));
                    } catch (Exception ex) {
                    }
                }
            }
        });

    }

    public void startAnimation() {
        loading.setVisible(true);
        this.animator = new Thread(new Runnable() {
            @Override
            public void run() {
                loading.setVisible(true);
            }
        });
        animator.start();
    }

    public void stopAnimation() {
        if (this.animator != null) {
            this.animator.stop();
        }
        loading.setVisible(false);

    }

    public void updateCount() {
        this.tcount.setText(ls.getTickets().size() + "");
        controller();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    public void controller() {
        this.loginbtn.setVisible(AuthentificationManager.isOnlineAuthenticated);
        lastTicketIndex = ls.getTickets().size() > 0 ? ls.getTickets().size() - 1 : -1;
        this.assignedTicketsMenu.setEnabled(AuthentificationManager.isOnlineAuthenticated);
        shtickets.setEnabled(ls.getTickets().size() > 0);
        cltickets.setEnabled(ls.getTickets().size() > 0);
        String tname = ls.getTickets() != null && ls.getTickets().size() > 0 ? ls.getTickets().get(ls.getTickets().size() - 1).name : "NA";
        jtckname.setVisible(!tname.equals("NA"));
        lbtname.setVisible(!tname.equals("NA"));
        lbtname.setText(tname);
        lastTicketIndex = ls.getTickets().size() == 1 ? 0 : lastTicketIndex;
        this.setIconImage(AvatarManager.getAvatar());
        this.exo.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/logo.png")).getImage()));
        this.loading.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/loading.gif")).getImage()));
        this.loading.setVisible(false);
        this.collaboration.setEnabled(AuthentificationManager.isOnlineAuthenticated);
        this.collaboration.setVisible(AuthentificationManager.isOnlineAuthenticated);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu4 = new javax.swing.JMenu();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jLabel1 = new javax.swing.JLabel();
        ticketname = new PlaceholderTextField();
        addTicket = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        tcount = new javax.swing.JLabel();
        exo = new javax.swing.JLabel();
        timerv = new javax.swing.JLabel();
        ltimer = new javax.swing.JLabel();
        jtckname = new javax.swing.JLabel();
        lbtname = new javax.swing.JLabel();
        loading = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        passask = new javax.swing.JMenuItem();
        loginbtn = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        expdir = new javax.swing.JMenuItem();
        reset = new javax.swing.JMenuItem();
        exit = new javax.swing.JMenuItem();
        mode = new javax.swing.JMenu();
        automode = new javax.swing.JRadioButtonMenuItem();
        passive = new javax.swing.JCheckBoxMenuItem();
        counter = new javax.swing.JCheckBoxMenuItem();
        browserl = new javax.swing.JCheckBoxMenuItem();
        TicketsMenu = new javax.swing.JMenu();
        shtickets = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        assignedTicketsMenu = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        cltickets = new javax.swing.JMenuItem();
        collaboration = new javax.swing.JMenu();
        showActiveAgents = new javax.swing.JMenuItem();
        mesagesswitch = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        about = new javax.swing.JMenuItem();
        update = new javax.swing.JMenuItem();

        jMenu4.setText("jMenu4");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("eXo Support Ticket Manager");
        setResizable(false);

        jLabel1.setText("Ticket Name:");

        addTicket.setForeground(new java.awt.Color(255, 153, 51));
        addTicket.setText("Add");
        addTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTicketActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(255, 153, 51));
        jLabel2.setText("Tickets Count:");

        tcount.setBackground(new java.awt.Color(255, 255, 255));
        tcount.setForeground(new java.awt.Color(102, 102, 102));
        tcount.setText("0");

        timerv.setText("00:00:00");
        timerv.setEnabled(false);

        ltimer.setForeground(new java.awt.Color(255, 153, 51));
        ltimer.setText("Timer:");
        ltimer.setEnabled(false);

        jtckname.setForeground(new java.awt.Color(255, 153, 51));
        jtckname.setText("Last Ticket:");

        lbtname.setText("NA");

        jMenu1.setText("File");

        passask.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        passask.setIcon(new javax.swing.ImageIcon(getClass().getResource("/passask.png"))); // NOI18N
        passask.setText("Switch Online Mode");
        passask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passaskActionPerformed(evt);
            }
        });
        jMenu1.add(passask);

        loginbtn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        loginbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/logintribe.png"))); // NOI18N
        loginbtn.setText("Log in to eXo Tribe");
        loginbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginbtnActionPerformed(evt);
            }
        });
        jMenu1.add(loginbtn);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/get.png"))); // NOI18N
        jMenuItem4.setText("Get eXo Platform");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        expdir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        expdir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/explore.png"))); // NOI18N
        expdir.setText("Explore Directory");
        expdir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expdirActionPerformed(evt);
            }
        });
        jMenu1.add(expdir);

        reset.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        reset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/reset.png"))); // NOI18N
        reset.setText("Reset ID");
        reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetActionPerformed(evt);
            }
        });
        jMenu1.add(reset);

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

        mode.setText("Mode");

        automode.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        automode.setText("Auto Mode");
        automode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/auto.png"))); // NOI18N
        automode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                automodeActionPerformed(evt);
            }
        });
        mode.add(automode);

        passive.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        passive.setText("Passive Mode");
        passive.setIcon(new javax.swing.ImageIcon(getClass().getResource("/passive.png"))); // NOI18N
        passive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passiveActionPerformed(evt);
            }
        });
        mode.add(passive);

        counter.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        counter.setText("Time Mode");
        counter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/timer.png"))); // NOI18N
        counter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                counterActionPerformed(evt);
            }
        });
        mode.add(counter);

        browserl.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        browserl.setSelected(true);
        browserl.setText("Browser Listening");
        browserl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/listening.png"))); // NOI18N
        browserl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browserlActionPerformed(evt);
            }
        });
        mode.add(browserl);

        jMenuBar1.add(mode);

        TicketsMenu.setText("Tickets");

        shtickets.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.ALT_MASK));
        shtickets.setIcon(new javax.swing.ImageIcon(getClass().getResource("/list.png"))); // NOI18N
        shtickets.setText("Show Timesheet");
        shtickets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shticketsActionPerformed(evt);
            }
        });
        TicketsMenu.add(shtickets);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/wait.png"))); // NOI18N
        jMenuItem1.setText("Available Tickets");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        TicketsMenu.add(jMenuItem1);

        assignedTicketsMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
        assignedTicketsMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assigned.png"))); // NOI18N
        assignedTicketsMenu.setText("Assigned Tickets");
        assignedTicketsMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                assignedTicketsMenuActionPerformed(evt);
            }
        });
        TicketsMenu.add(assignedTicketsMenu);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.META_MASK));
        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/metrics.png"))); // NOI18N
        jMenuItem3.setText("Global Metrics");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        TicketsMenu.add(jMenuItem3);

        cltickets.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK));
        cltickets.setIcon(new javax.swing.ImageIcon(getClass().getResource("/clear.png"))); // NOI18N
        cltickets.setText("Clear Timesheet");
        cltickets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clticketsActionPerformed(evt);
            }
        });
        TicketsMenu.add(cltickets);

        jMenuBar1.add(TicketsMenu);

        collaboration.setText("Collaboration");

        showActiveAgents.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SPACE, java.awt.event.InputEvent.ALT_MASK));
        showActiveAgents.setIcon(new javax.swing.ImageIcon(getClass().getResource("/activeagents.png"))); // NOI18N
        showActiveAgents.setText("Show Active Agents");
        showActiveAgents.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showActiveAgentsActionPerformed(evt);
            }
        });
        collaboration.add(showActiveAgents);

        mesagesswitch.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.ALT_MASK));
        mesagesswitch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/switchmessages.png"))); // NOI18N
        mesagesswitch.setText("Turn off Messages");
        mesagesswitch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mesagesswitchActionPerformed(evt);
            }
        });
        collaboration.add(mesagesswitch);

        jMenuBar1.add(collaboration);

        jMenu5.setText("?");

        about.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        about.setIcon(new javax.swing.ImageIcon(getClass().getResource("/about.png"))); // NOI18N
        about.setText("About");
        about.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutActionPerformed(evt);
            }
        });
        jMenu5.add(about);

        update.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_LESS, java.awt.event.InputEvent.CTRL_MASK));
        update.setIcon(new javax.swing.ImageIcon(getClass().getResource("/update.png"))); // NOI18N
        update.setText("Check for updates");
        update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateActionPerformed(evt);
            }
        });
        jMenu5.add(update);

        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tcount)
                        .addGap(18, 18, 18)
                        .addComponent(jtckname)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbtname)
                        .addGap(45, 45, 45)
                        .addComponent(ltimer)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timerv)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(loading))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(addTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ticketname, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(exo)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(ticketname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addTicket))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tcount)
                    .addComponent(exo)
                    .addComponent(timerv)
                    .addComponent(ltimer)
                    .addComponent(jtckname)
                    .addComponent(lbtname)
                    .addComponent(loading)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTicketActionPerformed
        // TODO add your handling code here:        startAnimation();
        startAnimation();
        doAddTicket();
        controller();
        stopAnimation();

    }//GEN-LAST:event_addTicketActionPerformed
    private void startTime() {
        if (this.counter.isSelected() && lbtname.getText().length() > 0 && !lbtname.getText().equals("NA")) {
            if (counterTimer == 0L) {
                timer = new Timer(1000, new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int hours = (int) counterTimer / 3600;
                        int minutes = (int) (counterTimer % 3600) / 60;
                        int seconds = (int) counterTimer % 60;

                        String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                        timerv.setText(String.valueOf(timeString));
                        counterTimer++;
                        //  if (counterTimer == 10) {
                        //timer.removeActionListener(this);
                        //     timer.stop();
                        // }
                    }
                });
                timer.start();
            } else {
                saveTime();

            }
        }
    }

    private void saveTime() {
        if (timer != null && lastTicketIndex != -1) {
            int hours = (int) counterTimer / 3600;
            ls.getTickets().get(lastTicketIndex).time = hours < 1 ? 1 : hours;
            // IO.saveList(ls);
            TicketManager.saveListTickets(ls);
        }
        timerv.setText("00:00:00");
        if (timer != null) {
            timer.stop();
        }
        counterTimer = 0L;
    }

    private boolean checkforUpdate() {
        //int last = IO.getLastUpVersion(this.serverURL);
        int last = UpdateManager.getLastVersion();
        if (last > this.VERSION) {
            if (JOptionPane.showConfirmDialog(this, "A newer version has been released! Would you like to update the application ?",
                    "Update: v " + (float) last / 1000 + " released !", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                UpdateManager.doUpdate(this.VERSION);
                //IO.doUpdate(this.VERSION);
            }
            return true;
        }
        return false;

    }

    private void doAddTicket() {
        // ls = IO.getList();
        ls = TicketManager.getListTickets();
        ls.showTickets();
        startTime();
        String tname = ticketname.getText().trim().toUpperCase();
        if (tname.length() < 3) {
            JOptionPane.showMessageDialog(this, "Please check the Ticket name!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Ticket t = new Ticket(tname, ls.getOwner());
        if (automode.isSelected()) {
            ls.addTicket(t, passive.isSelected());
            updateCount();
        } else if (!ls.addTicketC(t, passive.isSelected())) {
            JOptionPane.showMessageDialog(this, "Could not add the Ticket!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        // IO.saveList(ls);
        TicketManager.saveListTickets(ls);
        this.tcount.setText(ls.getTickets().size() + "");
        this.ticketname.setText("");

    }

    public void AddTicketFromHttp(String tckname) {
        // ls = IO.getList();
        ls = TicketManager.getListTickets();
        ls.showTickets();
        startTime();
        String tname = tckname.trim().toUpperCase();
        if (tname.length() < 3) {
            JOptionPane.showMessageDialog(this, "Please check the Ticket name!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Ticket t = new Ticket(tname, ls.getOwner());
        if (automode.isSelected()) {
            ls.addTicket(t, true);
            updateCount();
        } else if (!ls.addTicketC(t, passive.isSelected())) {
            JOptionPane.showMessageDialog(this, t.name + " Already added to Ticket List!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        // IO.saveList(ls);
        TicketManager.saveListTickets(ls);
        this.updateCount();
        java.awt.Toolkit.getDefaultToolkit().beep();
    }
    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_exitActionPerformed

    private void resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetActionPerformed
        // TODO add your handling code here:
        if (JOptionPane.showConfirmDialog(this, "Do you really want to restart saving tickets?", "Reset Tickets Save ?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            // IO.reset();
            TicketManager.reset();
            //ls = IO.getList();
            ls = TicketManager.getListTickets();
            this.tcount.setText(ls.getTickets().size() + "");
            //    this.setTitle("eXo Support Ticket Manager - " + IO.getUserName());
            this.setTitle("eXo Support Ticket Manager - " + TicketManager.getUserName());
            controller();
        }

    }//GEN-LAST:event_resetActionPerformed

    private void aboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this, "eXo Support Ticket Manager v " + (float) this.VERSION / 1000 + " \n Released By: Houssem - eXo 2019", "About", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_aboutActionPerformed

    private void shticketsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shticketsActionPerformed
        // TODO add your handling code here:
        ShowList sl = new ShowList(ls, this);
        sl.setVisible(true);

    }//GEN-LAST:event_shticketsActionPerformed

    private void expdirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expdirActionPerformed
        /* try {
            // TODO add your handling code here:
            Desktop.getDesktop().open(IO.getFile().getParentFile());
        } catch (IOException ex) {
        }*/
        TicketManager.exploreDirectory();
    }//GEN-LAST:event_expdirActionPerformed

    private void automodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_automodeActionPerformed
        // TODO add your handling code here:
        UISettings.setSettingValue(SettingType.AUTO_SAVE_MODE, automode.isSelected() ? "1" : "0");
        switchAutoMode();
    }//GEN-LAST:event_automodeActionPerformed

    private void switchAutoMode() {
        addTicket.setEnabled(!automode.isSelected());
        ticketname.setEnabled(!automode.isSelected());
        ticketname.setText("");
        addTicket.setText(automode.isSelected() ? "Auto" : "Add");
        ((PlaceholderTextField) ticketname).setPlaceholder(automode.isSelected() ? "Auto Mode: Waiting for a ticket..." : "Please input the ticket name");
        ClipoardListner();
    }
    private void clticketsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clticketsActionPerformed
        // TODO add your handling code here:
        if (ls.getTickets() == null || ls.getTickets().size() == 0) {
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Do really want to clear Tickets?", "Clear Tickets ?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            ls = new ListTickets(new LinkedList<Ticket>(), ls.getOwner());
            // IO.saveList(ls);
            TicketManager.saveListTickets(ls);
            updateCount();
        }
    }//GEN-LAST:event_clticketsActionPerformed

    private void updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateActionPerformed
        // TODO add your handling code here:
        if (!checkforUpdate()) {
            JOptionPane.showMessageDialog(this, "You are working on the last VERSION!", "No Update!", JOptionPane.PLAIN_MESSAGE);
        }
    }//GEN-LAST:event_updateActionPerformed

    private void counterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_counterActionPerformed
        // TODO add your handling code here:

        UISettings.setSettingValue(SettingType.TIMER_MODE, counter.isSelected() ? "1" : "0");
        switchAutoMode();
    }//GEN-LAST:event_counterActionPerformed
    private void switchTimerMode() {
        this.timerv.setEnabled(counter.isSelected());
        this.ltimer.setEnabled(counter.isSelected());
        this.jtckname.setText(counter.isSelected() ? "Work Ticket:" : "Last Ticket:");
        if (!counter.isSelected()) {
            saveTime();
        }
    }

    public void setAuthCred(LoginAuth auth) {
        this.auth = auth;
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        // if (!IO.isServiceUp()) {
        if (AuthentificationManager.isOnlineAuthenticated) {
            switch (NetworkUtils.login(auth.getUserName(), auth.getPassword())) {
                case -1:
                    JOptionPane.showMessageDialog(this, "Wrong password cannot show assigned tickets!", "Permission Denied!", JOptionPane.ERROR_MESSAGE);
                    return;
                case 0:
                    JOptionPane.showMessageDialog(this, "Cannot show Available Tickets!", "Service Unavailable!", JOptionPane.ERROR_MESSAGE);
                    return;
            }
        } else if (!NetworkUtils.isServiceUp()) {
            JOptionPane.showMessageDialog(this, "Cannot show Available Tickets!", "Service Unavailable!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (av == null) {
            av = new AvailableTickets(this);
        }
        av.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void assignedTicketsMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_assignedTicketsMenuActionPerformed
        // TODO add your handling code here:
        if (!TicketManager.hasPassword()) {
            JOptionPane.showMessageDialog(this, "You have to turn on the Online Mode!", "Permission Denied!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!NetworkUtils.isServiceUp()) {
            JOptionPane.showMessageDialog(this, "Cannot show Assigned Tickets!", "Service Unavailable!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (as == null) {
            as = new AssignedTickets(this);
        }
        as.setVisible(true);
    }//GEN-LAST:event_assignedTicketsMenuActionPerformed

    private void passiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passiveActionPerformed
        // TODO add your handling code here:
        UISettings.setSettingValue(SettingType.PASSIVE_SAVE_MODE, passive.isSelected() ? "1" : "0");
    }//GEN-LAST:event_passiveActionPerformed

    private void browserlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browserlActionPerformed
        // TODO add your handling code here:
        UISettings.setSettingValue(SettingType.BROWSER_LISTENING, browserl.isSelected() ? "1" : "0");
        browserListener();
    }//GEN-LAST:event_browserlActionPerformed

    private void passaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passaskActionPerformed
        // TODO add your handling code here:
        if (passask.getText().contains("Online")) { // to Disable
            passask.setText("Switch Offline Mode");
            UISettings.setSettingValue(SettingType.USER_PASSWORD, "1");
            if (NetworkUtils.isServiceUp()) {
                AuthentificationManager.setMain(this);
                this.setVisible(false);
                AuthentificationManager.checkAuthentication();
            }
        } else {
            if (JOptionPane.showConfirmDialog(this, "Are you sure to switch off the online mode ?", "Switch Offline Mode", JOptionPane.YES_OPTION) == JOptionPane.NO_OPTION) {
                return;
            }
            passask.setText("Switch Online Mode");
            UISettings.setSettingValue(SettingType.USER_PASSWORD, "0");
            AuthentificationManager.isOnlineAuthenticated = false;
        }
        controller();

    }//GEN-LAST:event_passaskActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        if (!NetworkUtils.isServiceUp()) {
            JOptionPane.showMessageDialog(this, "Cannot show Global Metrics!", "Service Unavailable!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Metrics mc = new Metrics();
        mc.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        gexo = gexo != null ? gexo : new GeteXoPlatform();
        gexo.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void loginbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginbtnActionPerformed
        // TODO add your handling code here:
        /*if (!AuthentificationManager.isOnlineAuthenticated) {
            return;
        }*/
        if (TicketManager.hasPassword()) {
            NetworkUtils.login2eXo(TicketManager.getUserID(), "nfs4speed3183");
        }
    }//GEN-LAST:event_loginbtnActionPerformed

    private void showActiveAgentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showActiveAgentsActionPerformed
        // TODO add your handling code here:
        cu = cu != null ? cu : new ConnectedUsers();
        cu.setVisible(true);
    }//GEN-LAST:event_showActiveAgentsActionPerformed

    private void mesagesswitchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mesagesswitchActionPerformed
        // TODO add your handling code here:
        UISettings.setSettingValue(SettingType.COLL_MESSAGES, mesagesswitch.getText().contains("on") ? "1" : "0");
        recieveMessages = mesagesswitch.getText().contains("on");
        if (mesagesswitch.getText().contains("on")) {
            mesagesswitch.setText("Turn off Messages");
        } else {
            mesagesswitch.setText("Turn on Messages");
        }

    }//GEN-LAST:event_mesagesswitchActionPerformed

    private void browserListener() {
        if (browserl.isSelected()) {
            tbrowser = tbrowser != null ? tbrowser : new Thread(new Runnable() {
                public void run() {
                    tp.init(me);
                    tp.start();
                    System.out.println("HTTP SERVER STARTED");
                }
            });
            tbrowser.start();
        } else if (tbrowser != null) {
            System.out.println("HTTP SERVER STOPPED");
            tp.stop();
            tbrowser.stop();
            tbrowser = null;
        }

    }

    private void checkMessages() {
        int delay = 10000;   // delay for 5 sec.
        int period = 30000;  // repeat every sec.
        java.util.Timer timer = new java.util.Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    if (recieveMessages) {
                        MessagesManager.listen();
                    }
                } catch (Exception ff) {
                }
            }
        }, delay, period);

    }

    public boolean isValidTicketName(String name) {
        int i = name.indexOf("-");
        if (i < 2) {
            return false;
        }
        String suffix = name.split("-")[1];
        String preffix = name.split("-")[0].trim().toUpperCase();
        if (preffix.length() > 10) {
            return false;
        }
        if (preffix.equals("PLF")) {
            return false;
        }
        if (preffix.contains("COMMUNITY") || preffix.contains("EXOPLATFORM")) {
            return false;
        }

        try {
            int d = Integer.parseInt(suffix);
            if (i > 1 && i < name.length() - 2 && d > 0) {
                return true;
            }
        } catch (Exception ex) {
        }
        return false;
    }

    private void ClipoardListner() {

        SYSTEM_CLIPBOARD = SYSTEM_CLIPBOARD != null ? SYSTEM_CLIPBOARD : Toolkit.getDefaultToolkit().getSystemClipboard();
        SYSTEM_CLIPBOARD.addFlavorListener(listener -> {

            try {
                if (!SYSTEM_CLIPBOARD.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                    return;
                }
                String clipboardText = (String) SYSTEM_CLIPBOARD.getData(DataFlavor.stringFlavor);

                //  SYSTEM_CLIPBOARD.setContents(new StringSelection(clipboardText), null);
                System.out.println("The clipboard contains: " + clipboardText);
                if (isValidTicketName(clipboardText)) {
                    if (ticketname.getText().trim().length() == 0) {
                        this.ticketname.setText(clipboardText.trim());
                        if (automode.isSelected()) {
                            doAddTicket();
                        }
                    } else {
                        java.awt.Toolkit.getDefaultToolkit().beep();
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

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
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    //   new Main().setVisible(true);
                    AuthentificationManager.checkAuthentication();
                    if (args != null && args.length > 0 && args[0].equals("_autoupdated")) {
                        JOptionPane.showMessageDialog(null, "You have successfully updated to version " + (float) VERSION / 1000 + " !", "Auto-Update Confirmation", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Please save your timesheet by the old VERSION, then DELETE tickets.exo file!", "File Version Mismatch", JOptionPane.ERROR_MESSAGE);
                    /* try {
                        Desktop.getDesktop().open(IO.getFile().getParentFile());
                    } catch (IOException ex1) {
                    }*/
                    TicketManager.exploreDirectory();
                    System.exit(-1);
                }

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu TicketsMenu;
    private javax.swing.JMenuItem about;
    private javax.swing.JButton addTicket;
    private javax.swing.JMenuItem assignedTicketsMenu;
    private javax.swing.JRadioButtonMenuItem automode;
    private javax.swing.JCheckBoxMenuItem browserl;
    private javax.swing.JMenuItem cltickets;
    private javax.swing.JMenu collaboration;
    private javax.swing.JCheckBoxMenuItem counter;
    private javax.swing.JMenuItem exit;
    private javax.swing.JLabel exo;
    private javax.swing.JMenuItem expdir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JLabel jtckname;
    private javax.swing.JLabel lbtname;
    private javax.swing.JLabel loading;
    private javax.swing.JMenuItem loginbtn;
    private javax.swing.JLabel ltimer;
    private javax.swing.JMenuItem mesagesswitch;
    private javax.swing.JMenu mode;
    private javax.swing.JMenuItem passask;
    private javax.swing.JCheckBoxMenuItem passive;
    private javax.swing.JMenuItem reset;
    private javax.swing.JMenuItem showActiveAgents;
    private javax.swing.JMenuItem shtickets;
    private javax.swing.JLabel tcount;
    private javax.swing.JTextField ticketname;
    private javax.swing.JLabel timerv;
    private javax.swing.JMenuItem update;
    // End of variables declaration//GEN-END:variables
}
