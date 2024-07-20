package com.github.omkumargithub.pkg.strategy;

import com.github.omkumargithub.pkg.domain.Server;

// import com.github.omkumargithub.pkg.strategy.Istrategy;

public class RoundRobin implements Istrategy {
  static   int current = 0;

    //  @Override
     public static Server next(Server[] servers) {
        int seen = 0;
        Server picked = null;

        while (seen < servers.length) {
            picked = servers[current];
            current = (current + 1) % servers.length;
            if (picked.isAlive()) {
                break;
            }
            seen++;
        }
        if (picked == null || seen == servers.length) {
            System.out.println("all servers are down");
            return null;
        }

        return picked;
    }
}
