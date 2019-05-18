package io;

import entity.ListTickets;
import entity.Ticket;
import java.util.Base64;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hba19
 */
public class TicketsFileManager {

    private File _fileStore = new File(System.getProperty("user.home") + "/.exotickets/offlineTickets.exo");
    private ListTickets _ticketsList;
    private PrintWriter _pw = null;
    private BufferedReader _br = null;

    private void checkFolder() {
        File st = getDirectory();
        if (!st.exists()) {
            st.mkdir();
        }
    }

    public TicketsFileManager() {
        checkFolder();
    }

    public File getDirectory() {
        return _fileStore.getParentFile();
    }

    public ListTickets getListTickets() {
        readTicketsFileStore();
        return _ticketsList;

    }

    private boolean isSaveFolderExists() {
        return _fileStore.getParentFile().exists();
    }

    private String serializeTicket(Ticket ticket) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String it = ticket.name + "|" + ticket.owner + "|" + dateFormat.format(ticket.creation) + "|" + ticket.time;
        return Base64.getEncoder().encodeToString(it.getBytes()) + "--" + it.getBytes().length;
    }

    private String serializeAgentID(String agent) {
        agent = agent.toLowerCase().trim();
        return Base64.getEncoder().encodeToString(agent.getBytes()) + "--" + agent.getBytes().length;
    }

    private Ticket unserializeTicket(String hash) {
        try {
            byte[] by = Base64.getDecoder().decode(hash.split("--")[0]);
            int crc = Integer.parseInt(hash.split("--")[1]);
            if (crc != by.length) {
                System.err.println("CRC Error: " + hash);
                return null;
            }
            String it = new String(by);
            String[] elts = it.split("\\|");
            Ticket ticket = new Ticket(elts[0], elts[1], elts[2], Integer.parseInt(elts[3]));
            return ticket;
        } catch (Exception ef) {
            return null;
        }
    }

    private String unserializeAgentID(String hash) {
        try {
            byte[] by = Base64.getDecoder().decode(hash.split("--")[0]);
            int crc = Integer.parseInt(hash.split("--")[1]);
            if (crc != by.length) {
                System.err.println("CRC Error: " + hash);
                return null;
            }
            String it = new String(by);
            return it;
        } catch (Exception ef) {
            return null;
        }
    }

    private String readAgentIDFileStore() {
        if (!isSaveFolderExists()) {
            System.err.println("Read Error: " + _fileStore.getParent() + " Not Found!");
            return "NoID";
        }
        try {
            _br = new BufferedReader(new FileReader(_fileStore));
            String line = null;
            String agentID = "NoID";
            while ((line = _br.readLine()) != null) {
                if (!line.startsWith("exoagent[")) {
                    continue;
                }
                String hash = line.replace("exoagent[", "").replace("]", "").trim();
                String elt = unserializeAgentID(hash);
                if (elt != null) {
                    agentID = elt;
                }
            }

            _br.close();
            return agentID;
        } catch (Exception e) {
            //e.printStackTrace();
            System.err.println("Error: Cannot read from " + _fileStore.getAbsolutePath() + " !");
            return "NoID";
        }

    }

    private void readTicketsFileStore() {
        LinkedList<Ticket> _tList = new LinkedList<Ticket>();
        //   _ticketsList = ListTickets();
        if (!isSaveFolderExists()) {
            System.err.println("Read Error: " + _fileStore.getParent() + " Not Found!");
            return;
        }
        try {
            _br = new BufferedReader(new FileReader(_fileStore));
            String line = null;
            while ((line = _br.readLine()) != null) {
                if (!line.startsWith("exoticket[")) {
                    continue;
                }
                String hash = line.replace("exoticket[", "").replace("]", "").trim();
                Ticket elt = unserializeTicket(hash);
                if (elt != null) {
                    _tList.add(elt);
                }
            }
            _br.close();
            _ticketsList = new ListTickets(_tList, readAgentIDFileStore());

        } catch (Exception e) {
            //e.printStackTrace();
            System.err.println("Error: Cannot read from " + _fileStore.getAbsolutePath() + " !");
            return;
        }

    }

    private boolean saveAgentIDFileStore(String agentID) {
        if (agentID == null || agentID.length() < 2) {
            return false;
        }
        try {
            _pw = new PrintWriter(new FileWriter(_fileStore));
            _pw.write("exoagent[" + serializeAgentID(agentID.toLowerCase().trim()) + "]\n");
            return true;
        } catch (IOException ex) {
            System.err.println("Error: Cannot save Agent to " + _fileStore.getAbsolutePath() + " !");
            return false;
        }
    }

    private boolean saveTicketsFileStore() {
        if (!isSaveFolderExists()) {
            System.err.println("Write Error: " + _fileStore.getParent() + " Not Found!");
            return false;
        }
        try {
            _pw = _pw != null ? _pw : new PrintWriter(new FileWriter(_fileStore));
            for (Ticket item : _ticketsList.getTickets()) {
                _pw.write("exoticket[" + serializeTicket(item) + "]\n");
            }
            _pw.close();
            return true;
        } catch (Exception e) {
            //e.printStackTrace();
            System.err.println("Error: Cannot save to " + _fileStore.getAbsolutePath() + " !");
            return false;
        }

    }

    public boolean setListTickets(ListTickets ticketsList, String agentid) {
        _ticketsList = ticketsList;
        return saveAgentIDFileStore(agentid) && saveTicketsFileStore();
    }

    //public  String getPreferences
    public void main(String[] args) {
        LinkedList<Ticket> ll = new LinkedList<Ticket>();

        for (int i = 0; i < 10; i++) {
            ll.add(new Ticket("Name" + i, "Owner" + i));
        }
        // setListTickets(new ListTickets(ll, "houssem.benali"), "houssem.benali");
        ListTickets out = getListTickets();
        if (out == null) {
            return;
        }
        System.out.println(out.getOwner());
        for (Ticket t : out.getTickets()) {
            System.out.println(t.name + "--" + t.owner + "---" + t.creation + "--" + t.time + "---" + t.isTribe);
        }

    }

}
