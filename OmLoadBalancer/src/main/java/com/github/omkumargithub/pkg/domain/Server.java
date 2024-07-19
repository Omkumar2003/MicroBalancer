package com.github.omkumargithub.pkg.domain;


import java.util.*;
import java.net.*;
// import java.net.http.*;
// import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.github.omkumargithub.helper.ReverseProxy;

public class Server {
   public URL url;
    ReverseProxy proxy;
    HashMap<String, String> metaData;
    boolean alive;
    ReentrantReadWriteLock mu;

    public  void Forward() {
        // proxy.serveHttp();
    }

    

    public Server(URL url, ReverseProxy proxy, HashMap<String,String> metaData) {
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

