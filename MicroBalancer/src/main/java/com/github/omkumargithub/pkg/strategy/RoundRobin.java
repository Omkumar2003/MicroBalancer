package com.github.omkumargithub.pkg.strategy;

import java.util.List;

import com.github.omkumargithub.pkg.domain.Server;

// import com.github.omkumargithub.pkg.strategy.Istrategy;

public class RoundRobin implements Istrategy {
     int current = 0;

     @Override
     public  Server next(List<Server> servers) {


        for(int i = 0 ; i<servers.size();i++){
            Server server = servers.get(current);
            current = (current + 1) % servers.size();
            if (server.isAlive()) {
                return server;
            }

        }
        System.out.println("All servers are down");
        return null;
    }

    
}
