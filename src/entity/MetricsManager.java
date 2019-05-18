/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import io.BasicUtils;
import io.MetricsNetManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author hba19
 */
public class MetricsManager {

    private Date[] dateList = null;
    private String[] statusList = null;
    private int index = 0;
    private Date min;
    private Date max;

    private boolean isInclude(Date d) {
        return d.after(min) && d.before(max);
    }

    private void sync() {
        while (index < dateList.length && !dateList[index++].after(min));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(sdf.format(dateList[index]));
    }

    private boolean lessThan(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(max);
        c.add(Calendar.DATE, 1); // Adding 1 days
        max = c.getTime();
        return d.before(max);
    }

    public Date getMax() {
        Calendar c = Calendar.getInstance();
        c.setTime(max);
        c.add(Calendar.DATE, -1); // Adding 1 days
        return c.getTime();
    }

    public Date getMin() {
        Calendar c = Calendar.getInstance();
        c.setTime(min);
        c.add(Calendar.DATE, 1); // Adding 1 days
        return c.getTime();
    }

    private void nextWeek() {
        Calendar c = Calendar.getInstance();
        c.setTime(min);
        c.add(Calendar.DATE, 7);
        min = c.getTime();
        c.setTime(max);
        c.add(Calendar.DATE, 7);
        max = c.getTime();
    }

    public MetricsManager() {
        MetricsNetManager mnm = new MetricsNetManager();
        dateList = mnm.getDates();
        statusList = mnm.getStatus();
        try {
            min = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-1");
            max = new SimpleDateFormat("yyyy-MM-dd").parse("2019-02-9");
        } catch (ParseException ex) {
            min = Calendar.getInstance().getTime();
            max = Calendar.getInstance().getTime();
        }
    }

    private Date[] getDate() {
        return dateList;
    }

    private boolean isInNextWeek(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(min);
        c.add(Calendar.DATE, 7);
        return d.after(c.getTime());
    }

    public String[] calculate() {
        sync();
        int opencounter = 0;
        int wpfcounter = 0;
        int wficounter = 0;
        int closedcounter = 0;
        int consideredcounter = 0;
        int week = 7;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        ArrayList<String> ls = new ArrayList<String>();
        for (int i = 0; i < dateList.length; i++) {
            if (isInclude(dateList[i])) //{
            {
                if (!statusList[i].equals("closed")) {
                    opencounter++;
                }
                if (statusList[i].equals("closed")) {
                    closedcounter++;
                }
                if (statusList[i].equals("considered")) {
                    consideredcounter++;
                }

                if (statusList[i].equals("resolved_maintenance")) {
                    wpfcounter++;
                }
                if (statusList[i].equals("suspended_wfi")) {
                    wficounter++;
                }

            } else if (isInNextWeek(dateList[i])) {
                ls.add((week<10 ?"0"+week:week) + "|" + sdf.format(getMin()) + "|" + sdf.format(getMax()) + "|" + opencounter + "|"+ consideredcounter + "|" + wpfcounter + "|" + wficounter + "|" + closedcounter);
                opencounter = 0;
                wpfcounter = 0;
                wficounter = 0;
                consideredcounter = 0;
                closedcounter = 0;
                week++;
                nextWeek();
                i--;
            }
        }
        return BasicUtils.to2DStr(ls.toArray());
    }

    public static void main(String[] args) {
        MetricsManager mm = new MetricsManager();
        Date[] d = mm.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        mm.sync();
        for (int i = 0; i < d.length; i++) {
            if (mm.isInclude(d[i])) //{
            {
                System.out.println("        " + sdf.format(d[i]));
            } else {
                if (mm.isInNextWeek(d[i])) {
                    mm.nextWeek();
                    System.out.println(sdf.format(mm.getMin()) + "---------" + sdf.format(mm.getMax()));
                    i--;
                }
            }

        }
    }

}
