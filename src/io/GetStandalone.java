/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

/**
 *
 * @author Famille
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class GetStandalone {

    private File plfdir = new File(System.getProperty("user.home") + "/platforms/exomanager/");

    private void checkFolder() {
        File platform = new File(System.getProperty("user.home") + "/platforms/");
        if (!platform.exists()) {
            platform.mkdir();
        }
        File exomanager = new File(System.getProperty("user.home") + "/platforms/exomanager/");
        if (!exomanager.exists()) {
            exomanager.mkdir();
        }
    }

    public File downloadPLF(String plfname, boolean isTomcat, JProgressBar jp) {
        checkFolder();
        String saveFilePath = null;
        try {
            URL url;
            InputStream is = null;
            BufferedReader br;
            String line;
            String servertype = isTomcat ? "tomcat" : "jbosseap";
            // Install Authenticator
            RepositoyAuth.setPasswordAuthentication("hbenali", "nfs4speed3183");
            Authenticator.setDefault(new RepositoyAuth());
            url = new URL("https://repository.exoplatform.org/content/groups/private/com/exoplatform/platform/distributions/plf-enterprise-" + servertype + "-standalone/" + plfname + "/plf-enterprise-" + servertype + "-standalone-" + plfname + ".zip");
            // url = new URL("http://exosp.alwaysdata.net/");
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String fileName = "";
                String disposition = httpConn.getHeaderField("Content-Disposition");
                String contentType = httpConn.getContentType();
                int contentLength = httpConn.getContentLength();

                if (disposition != null) {
                    // extracts file name from header field
                    int index = disposition.indexOf("filename=");
                    if (index > 0) {
                        fileName = disposition.substring(index + 10,
                                disposition.length() - 1);
                    }
                } else {
                    // extracts file name from URL
                    fileName = url.getPath().substring(url.getPath().lastIndexOf("/") + 1,
                            url.getPath().length());
                }

                System.out.println("Content-Type = " + contentType);
                System.out.println("Content-Disposition = " + disposition);
                System.out.println("Content-Length = " + contentLength);
                System.out.println("fileName = " + fileName);
                jp.setToolTipText("Click here to Abort");
                InputStream inputStream = httpConn.getInputStream();
                saveFilePath = plfdir + File.separator + fileName;
                FileOutputStream outputStream = new FileOutputStream(saveFilePath);
                long downloadedFileSize = 0;
                int x = 0;
                byte[] buffer = new byte[1024];
                while ((x = inputStream.read(buffer)) >= 0) {
                    outputStream.write(buffer, 0, x);
                    downloadedFileSize += x;
                    //  int currentProgress = (int) ((((double) downloadedFileSize) / ((double) contentLength)) * 100000d);
                    final int currentProgress = (int) ((double) downloadedFileSize);
                    final int xp = Math.round((currentProgress * 100) / contentLength);
                    //     System.out.println(currentProgress + "---" + contentLength+"---"+xp+" %");
//    int currentProgress = (int) (downloadedFileSize * 100)/ contentLength;
                    //              grabs.downloadprogress.setPreferredSize();
                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                if (jp != null) {
                                    //int xp = (int) (currentProgress * 100) / contentLength;
                                    jp.setValue(xp);
                                }
                            }
                        });
                    } catch (Exception e) {

                    }
                }

                outputStream.close();
                inputStream.close();

                System.out.println("File downloaded");
            } else {
                System.out.println("No file to download. Server replied HTTP code: " + responseCode);
            }
            httpConn.disconnect();
        } catch (IOException ex) {
            Logger.getLogger(GetStandalone.class.getName()).log(Level.SEVERE, null, ex);
        }
        return saveFilePath != null ? new File(saveFilePath) : null;
    }

    public ArrayList<String> getPLFList(boolean isTomcat) {
        ArrayList<String> plflist = new ArrayList<String>();
        try {
            URL url;
            InputStream is = null;
            BufferedReader br;
            String line;
            String servertye = isTomcat ? "tomcat" : "jbosseap";
            RepositoyAuth.setPasswordAuthentication("hbenali", "nfs4speed3183");
            Authenticator.setDefault(new RepositoyAuth());
            url = new URL("https://repository.exoplatform.org/content/groups/private/com/exoplatform/platform/distributions/plf-enterprise-" + servertye + "-standalone/");
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                String strline = null;
                while (null != (strline = in.readLine())) {
                    if (!strline.contains("<td><a href=\"https://repository.exoplatform.org/content/groups/private/com/exoplatform/platform/distributions/")) {
                        continue;
                    }
                    if (strline.contains("xml")) {
                        continue;
                    }
                    if (strline.contains("maven")) {
                        continue;
                    }
                    int p = strline.indexOf("/\">");
                    int q = strline.indexOf("/</a></td>");
                    plflist.add(strline.substring(p + 3, q));
                }

            } else {
                System.out.println("No file to download. Server replied HTTP code: " + responseCode);
            }
            httpConn.disconnect();
        } catch (IOException ex) {
            Logger.getLogger(GetStandalone.class.getName()).log(Level.SEVERE, null, ex);
        }
        return plflist;
    }
}
