package pkg.domain;

import java.util.*;
import java.net.*;
import java.net.http.*;
// import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server {
    URL url;
    // Right now I don't know how to set up reverse proxy my guess is I have to use
    // some third party software
    HashMap<String, String> metaData;
    boolean alive;
    ReentrantReadWriteLock mu;

    public static void Forward(HttpResponse res, HttpRequest req) {

    }

    public String GetMetaOrDefault(String key, String defVal) {
        boolean temp = false;
        String val = defVal;
        metaData.forEach((k, v) -> {
            if (k == key) {
                val = v;
                temp = true;
                return;
            }
        });
        return defVal;
    }


    
}

class Replica {
    String url;
    HashMap<String, String> metaData;
}

class Service {
    String name;
    String matcher;
    String strategy;
    Replica[] replicas;

}

class Config {
    Service[] services;
    String startegy;
}