package com.github.omkumargithub.pkg.health;


import com.github.omkumargithub.pkg.domain.Server;
import java.net.UnknownHostException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
// import pkg.domain.Server;
import java.util.*;;

public class Checker {
    List<Server> servers = new ArrayList<>();
    int period;

    public Checker(List<Server> servers){
        this.servers = servers;

    }

    public void start() {

    
        System.out.println("Intialising Health Service for First Time............");
        while (true) {
            for (int i = 0; i < servers.size(); i++) {
                final int serverIndex = i;
                // We are not able to access I inside the thread that's why we are declaring this here
                
                Thread T = new Thread(() -> {
                    checkHealth(servers.get(serverIndex)); 
                    // Just try to access some index it will not work we are not equal to access outside variable until its final
                    // I think it is done so that there can be a consistency in the values 
                });
                T.start();
            }
            
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                // inform admin if there is no admin then u r offically cooked
            }
        }

    }

    public void checkHealth(Server server) {
 
        try {
            URL url = new URL(server.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // connection.setRequestProperty("om", "true");
            connection.setRequestProperty("X-Health-Check", "true");


            int responseCode = connection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                // AfterCheckedValue = true;
                if(!server.isAlive()){
                    server.setLiveness(true);
                    System.out.println("    Server  " + server.url +"    SERVER IS DEAD");
                }else{
                    System.out.println("    Server  " + server.url +"   SERVER WORKING CORRECTCLY");
                }
            
            }
            else{
                    server.setLiveness(false);
                    System.out.println("    Server " + server.url +"RIP.......");

            }
            // // phele ka server check krna hai .....if needed we should update it 
            // if(!server.isAlive()){
            //     server.setLiveness(true);
            //     System.out.println("dead came to life");
            // }else{
            // server.setLiveness(true);
            //     System.out.println("life is going well");
            // }
            // socket.close();

        } 
        // catch (UnknownHostException e) {
        //     server.setLiveness(false);
        //     System.out.println("RIP.......");

        // }
         catch (IOException e) {
            server.setLiveness(false);
            System.out.println("RIP.......");
        }

    }
}

