package com.github.omkumargithub.pkg.strategy;

import java.util.List;

import com.github.omkumargithub.pkg.domain.Server;

// import com.github.omkumargithub.pkg.strategy.Istrategy;

public class RoundRobin implements Istrategy {
     int current = 0;

     @Override
     public  Server next(List<Server> servers) {
        int seen = 0;
        Server picked = null;

        while (seen < servers.size()) {

            picked = servers.get(current);
            current = (current + 1) % servers.size();
            if (picked.isAlive()) {
                break;
            }
            seen++;
        }
        if (picked == null || seen == servers.size()) {
            System.out.println("all servers are down");
            return null;
        }

        return picked;
    }

    
}
