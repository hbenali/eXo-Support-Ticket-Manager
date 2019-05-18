package entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hba19
 */
public class Ticket implements Serializable, Comparable<Ticket> {

    public String name;
    public Date creation;
    public String owner;
    public Boolean isTribe;
    public int time = 2;

    private String captilaze(String name) {
        return name.trim().substring(0, 1).toUpperCase() + name.trim().substring(1).toLowerCase();
    }

    private void setTribe() {
        this.isTribe = this.name.toUpperCase().contains("TRIBE");
    }

    public Ticket(String name, String owner) {
        this.name = name.toUpperCase().trim();
        this.owner = owner;
        Calendar cal = Calendar.getInstance();
        this.creation = cal.getTime();
        setTribe();
    }

    public Ticket(String name, String owner, String create, int hour) {
        this.name = name.toUpperCase().trim();
        this.owner = owner;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Calendar cal = Calendar.getInstance();
        try {
            this.creation = dateFormat.parse(create);
        } catch (ParseException ex) {
            ex.printStackTrace();
            this.creation = cal.getTime();
        }
        this.time = hour;
        setTribe();
    }

    @Override
    public boolean equals(Object t) {
        try {
            return ((Ticket) t).name.equals(name);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int compareTo(Ticket t) {
        try {
            return ((Ticket) t).name.equals(name) ? 0 : -1;
        } catch (Exception e) {
            return 1;
        }
    }

}
