package entity;


import io.IOold;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
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
public class ListTickets implements Serializable {

    private List<Ticket> lstickets;
    private String owner;

    public ListTickets(List ls, String owner) {
        this.owner = owner;
        this.lstickets = ls != null ? ls : new LinkedList<Ticket>();
        System.out.println("Ticket List:::" + owner);
    }

    public List<Ticket> getTickets() {
        return lstickets;
    }

    public String getOwner() {
        return owner;
    }
    
    public void setOwner(String owner){
        this.owner = owner;
    }

    public void addTicket(Ticket t, boolean... passive) {
        boolean isPassive = passive != null && passive.length == 1 && passive[0] == true;

        // if (lstickets.size() == 0 || !lstickets.get(lstickets.size() - 1).equals(t)) {
        if ((isPassive || isValidTicket(t)) && acceptAdd(t)) {
            this.lstickets.add(t);
        }
    }

    public boolean addTicketC(Ticket t, boolean... passive) {
        boolean isPassive = passive != null && passive.length == 1 && passive[0] == true;
        // if (lstickets.size() == 0 || !lstickets.get(lstickets.size() - 1).equals(t)) {
        if ((isPassive || isValidTicket(t)) && acceptAdd(t)) {
            this.lstickets.add(t);
            return true;
        }
        return false;
    }

    public void showTickets() {
        for (Ticket t : lstickets) {
            System.out.println(t.name + "---" + t.owner + "---" + t.creation + "--" + t.isTribe);
        }

    }

    private boolean hasSameDate(Ticket t1, Ticket t2) {
        try {
            DateFormat fulldate = new SimpleDateFormat("dd/MM/yyyy");
            return fulldate.format(t1.creation).equals(fulldate.format(t2.creation));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean acceptAdd(Ticket tck) {
        for (Ticket t : lstickets) {
            if (tck.name.trim().toUpperCase().equals(t.name.trim().toUpperCase()) && hasSameDate(t, tck)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidTicket(Ticket t) {
        return IOold.isValidTicket(t);
    }

}
