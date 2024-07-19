package cmd.omLoadBalancer;

import java.net.URL;
import java.net.http.HttpRequest;
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
    Map<String, ServerList> omServerList;

    public ok(Config conf) {
        Map<String, ServerList> ServerMap = new HashMap<String, ServerList>();

        for (int i = 0; i < conf.services.length; i++) {
            ArrayList<Server> servers = new ArrayList<Server>();

            for (int j = 0; i < conf.services[i].replicas.length; j++) {
                URL ur = conf.services[i].replicas[j].url;
                // making proxy
                ReverseProxy rp = new ReverseProxy();
                servers.add(new Server(ur, rp, conf.services[i].replicas[j].metaData));
            }

            Checker newChecker = new Checker(servers);
            ServerMap.put(conf.services[i].matcher, new ServerList(
                    servers,
                    conf.services[i].name,
                    new Rb(),
                    newChecker));

        }

        ServerMap.forEach((k, v) -> {
            v.healthChecker.start();
        });

        this.config = conf;
        this.omServerList = ServerMap;
    }
// it is not returning full serverlist ....it is just a name for omserverlist data's value type
    public  ServerList findServiceList(String reqPath) {
        final ServerList temp = null;
        omServerList.forEach((k, v) -> {
            if (reqPath.startsWith(k)) {
                System.out.println("url found");
                temp = v;
            }
            
        });
        return temp;
    }

    public void serveHttp(HttpRequest req) {
        // 
        ServerList sl = findServiceList(req.uri().getPath());
        // 
        // i know worst implementation......i can solve it .....but it will take time
        Server next = sl.strategy.next(sl.servers);

        // 
        next.Forward();
        // 


    }

}

public class Main {
    public static void main(String[] args) {

    }
}
