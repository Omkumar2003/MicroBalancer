package cmd.omLoadBalancer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pkg.config.Config;

import pkg.domain.Service;
import pkg.domain.Server;
import helper.ReverseProxy;
import pkg.health.Checker;
import pkg.config.ServerList;
import pkg.strategy.Rb;


class ok {
    Config config;
    Map<String, ServerList> ServerList;

    public void ok(Config conf) {
        Map<String, ServerList> ServerMap = new HashMap<String, ServerList>();

        for (int i = 0; i < conf.services.length; i++) {
            ArrayList<Server> servers = new ArrayList<Server>();

            for (int j = 0; i < conf.services.replicas.length; j++) {
                String ur = conf.services.replicas[j].url;
                // making proxy
                ReverseProxy rp = new ReverseProxy();
                servers.add(new Server(ur, rp, conf.services.replicas[j].metaData));
            }

            Checker newChecker = new Checker(servers);
            ServerMap.put(conf.services[i].matcher, new ServerList(
                    servers,
                    conf.services[i].name,
                    new Rb(),
                    newChecker));

        }
        
        ServerMap.forEach((k,v)->{
            v.healthChecker.start();
        });


        this.config= conf;
        this.ServerList= ServerMap;
    }

}

public class Main {

}
