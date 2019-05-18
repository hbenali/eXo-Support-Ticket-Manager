/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hba19
 */
public class MetricsNetManager {
    Date[] dates;
    String[] status;

    private Date parseDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(date);
        } catch (ParseException ex) {
            return null;
        }
    }

    public MetricsNetManager() {
        calculate();
    }
    

    private void calculate() {
        try {
            ArrayList<Date> ar = new ArrayList<Date>();
            ArrayList<String> st = new ArrayList<String>();
            URL url = new URL("http://exosp.alwaysdata.net/getMetrics.php?raw=1");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while (null != (line = br.readLine())) {
                String[] item = line.trim().replace("<br>", "").split("\\|");
                ar.add(parseDate(item[0]));
                st.add(item[1]);
            }
            dates =  BasicUtils.to2DDate(ar.toArray());
            status = BasicUtils.to2DStr(st.toArray());
        } catch (Exception ex) {
        }
    }
    public Date[] getDates(){
        return dates;
    }
    public String[] getStatus(){
        return status;
    }
    
}
