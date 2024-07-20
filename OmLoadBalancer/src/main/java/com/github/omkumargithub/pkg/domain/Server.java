package com.github.omkumargithub.pkg.domain;


import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
// import java.net.http.*;
// import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.github.omkumargithub.helper.ReverseProxy;

public class Server {
   public String url;
   ServerSocket proxy;
    HashMap<String, String> metaData;
    boolean alive;
    ReentrantReadWriteLock mu;

    public  String Forward() {

        // Actually this will have business logic but right now we will just return a simple string
        return "hello world ";

        // proxy.serveHttp();
    }

        public void handleClient(Socket clientSocket) throws IOException {
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String request = in.readLine();
            System.out.println("Request received................ " + request);

            OutputStream out = clientSocket.getOutputStream();
            String responseBody = "response from the load balancer";

            // this is copied .......bcoz i am not learnning this syntax
            String temp = "HTTP/1.1 200 OK\r\n"
                    + "Content-Length: " + responseBody.getBytes().length + "\r\n"
                    + "Content-Type: text/plain\r\n"
                    + "\r\n"
                    + responseBody;

            out.write(temp.getBytes());
            out.flush();
            out.close();
            in.close();
            // seeee........For the specific food the connexion is closed
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    public Server(String url, ServerSocket proxy, HashMap<String,String> metaData) {
        this.url = url;
        this.proxy = proxy;
        this.metaData = metaData;
    }



    
    public String GetMetaOrDefault(String key, String defVal) {
        String val = defVal;
        // for each 
        // type eachElement: iterateObject
        // you cannot do 
        //         for (HashMap<String, String> entry : metaData) {
        // HashMap - Getting "Can only iterate over an array or an instance of java.lang.Iterable"
        // enumerate over entryset

        for (Map.Entry<String, String> entry : metaData.entrySet()) {
            if (entry.getKey().equals(key)) {
                val = entry.getValue();
                break;
            }
        }
        return val;
    }

    // 
    public int GetMetaOrDefaultInt(String key, int defaultVal) {
        String temp = "" + defaultVal + "";
        String val = GetMetaOrDefault(key, temp);
        try {
            int temp1 = Integer.parseInt(val);
            return temp1;

        } catch (NumberFormatException e) {
            return defaultVal;
        }

    }

    public boolean setLiveness(boolean value) {
        try {
            mu.writeLock().lock();
            alive = value;
            // return alive;
        } catch (Exception e) {
        } finally {
            mu.writeLock().unlock();
        }
        return alive;

    }

    public boolean isAlive() {

        try {
            mu.readLock().lock();
        } catch (Exception e) {
        } finally {
            mu.readLock().unlock();
        }
        ;

        return alive;

    }

}

