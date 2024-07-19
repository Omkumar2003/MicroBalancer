package com.github.omkumargithub;


import java.net.URL;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.github.omkumargithub.pkg.config.Config;
import com.github.omkumargithub.pkg.domain.Service;
import com.github.omkumargithub.pkg.domain.Server;
import com.github.omkumargithub.helper.ReverseProxy;
import com.github.omkumargithub.pkg.health.Checker;
import com.github.omkumargithub.pkg.config.ServerList;
import com.github.omkumargithub.pkg.strategy.Istrategy;
import com.github.omkumargithub.pkg.strategy.RoundRobin;


class ok {
    Config config;
    HashMap<String, ServerList> omServerList;

    public ok(Config conf) {
        HashMap<String, ServerList> ServerMap = new HashMap<>();

        for (int i = 0; i < conf.services.size(); i++) {
            ArrayList<Server> servers = new ArrayList<>();

            for (int j = 0; j < conf.services.get(i).replicas.size(); j++) {
                URL ur = conf.services.get(i).replicas.get(j).url;
                // making proxy
                ReverseProxy rp = new ReverseProxy();
                servers.add(new Server(ur, rp, conf.services.get(i).replicas.get(j).metaData));
            }

            Checker newChecker = new Checker(servers);
            ServerMap.put(conf.services.get(i).matcher, new ServerList(
                    servers,
                    conf.services.get(i).name,
                    new RoundRobin(),
                    newChecker));

        }

        ServerMap.forEach((k, v) -> {
            v.healthChecker.start();
        });

        this.config = conf;
        this.omServerList = ServerMap;
    }
    
// it is notreturning full serverlist ....it is just a name for omserverlist data's value type

public  ServerList findServiceList(String reqPath) {
         ServerList temp = null;


         for (Map.Entry<String, ServerList> entry : omServerList.entrySet()){
            if (reqPath.startsWith(entry.getKey())) {
                      System.out.println("url found");
                     temp = entry.getValue();
                     }
         }
        // omServerList.forEach((k, v) -> {
        //     if (reqPath.startsWith(k)) {
        //         System.out.println("url found");
        //         temp = v;
        //     }
            
        // });
        return temp;
    }

    public void serveHttp(HttpRequest req) {
        // 
        ServerList sl = findServiceList(req.uri().getPath());
        // 
        // i know worst implementation......i can solve it .....but it will take time
        // Server next = sl.strategy.next(sl.servers);

        // 
        // next.Forward();
        // 


    }

}

public class Main {
    public static void main(String[] args) {

    }
}
