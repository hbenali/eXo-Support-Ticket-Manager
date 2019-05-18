package io;
import entity.ListTickets;
import entity.Ticket;
import entity.TicketManager;
import gui.Main;
import java.net.*;
import java.io.*;
import java.util.*;

public class TicketHttpServer {

     final int port = 19999;
     final String newLine = "\r\n";
    private Main main;
    private ServerSocket socket=null;
    
    public void init(Main m){
        this.main=m;
    }

    public static void main(String[] args) {
        new TicketHttpServer().start();
    }
    public void stop(){
        try{
        if(socket!=null)
        socket.close();
        }catch(Exception e){
           System.err.println("Could not stop server: ");
        }
    }
    public void start() {
        try {
           socket = new ServerSocket(port);

            while (true) {
                Socket connection = socket.accept();

                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    OutputStream out = new BufferedOutputStream(connection.getOutputStream());
                    PrintStream pout = new PrintStream(out);

                    // read first line of request
                    String request = in.readLine();
                    if (request == null) {
                        continue;
                    }

                    // we ignore the rest
                    while (true) {
                        String ignore = in.readLine();
                        if (ignore == null || ignore.length() == 0) {
                            break;
                        }
                    }

                    if (!request.startsWith("GET ")
                            || !(request.endsWith(" HTTP/1.0") || request.endsWith(" HTTP/1.1"))) {
                        // bad request
                        pout.print("HTTP/1.0 400 Bad Request" + newLine + newLine);
                    } else {
                        String response;
                        if (request.contains("GET /t=")) {
                            String tck = request.replace("HTTP/1.1", "").replace("GET /t=", "").trim();
                            main.AddTicketFromHttp(tck);
                            response = tck+" Added!";
                        } else {
                            response = "OK";
                        }

                        pout.print(
                                "HTTP/1.0 200 OK" + newLine
                                + "Content-Type: text/plain" + newLine
                                + "Date: " + new Date() + newLine
                                + "Content-length: " + response.length() + newLine + newLine
                                + response
                        );
                    }

                    pout.close();
                } catch (Throwable tri) {
                    System.err.println("Error handling request: " + tri);
                }
            }
        } catch (Throwable tr) {
            System.err.println("Could not start server: " + tr);
        }
    }
}
