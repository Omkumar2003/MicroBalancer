package com.github.omkumargithub.pkg.strategy;

import java.util.List;

import com.github.omkumargithub.pkg.domain.Server;

public class WeightedRoundRobin implements Istrategy {

    int[] count = null;
    int cur = 0;

    @Override
    public  Server next(List<Server> servers) {

        // first time
        if (count == null) {
            count = new int[servers.size()];
            cur = 0;
        }

        int seen = 0;
        Server picked = null;
        while (seen < (servers.size())) {
            picked = servers.get(cur);
            int capacity = picked.GetMetaOrDefaultInt("weight", 1);

            if (!picked.isAlive()) {
                seen += 1;
                count[cur] = 0;
                cur = (cur + 1) % servers.size();
                continue;
            }

            if (count[cur] <= capacity) {
                count[cur] += 1;
                return picked;
            }

            count[cur] = 0;
            cur = (cur + 1) % servers.size();
        }

        if (picked == null || seen == servers.size()) {
            System.out.println("all servers are down");
            return null;
        }

        return picked;
    }

}
