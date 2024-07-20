package com.github.omkumargithub.pkg.strategy;

import com.github.omkumargithub.pkg.domain.Server;

public class WeigthedRoundRobin implements Istrategy {
   static int[] count = null;
   static int cur = 0;

    // @Override
    public static Server next(Server[] servers) {

        // first time
        if (count == null) {
            count = new int[servers.length];
            cur = 0;
        }

        int seen = 0;
        Server picked = null;
        while (seen < (servers.length)) {
            picked = servers[cur];
            int capacity = picked.GetMetaOrDefaultInt("weight", 1);

            if (!picked.isAlive()) {
                seen += 1;
                count[cur] = 0;
                cur = (cur + 1) % servers.length;
                continue;
            }

            if (count[cur] <= capacity) {
                count[cur] += 1;
                return picked;
            }

            count[cur] = 0;
            cur = (cur + 1) % servers.length;
        }

        if (picked == null || seen == servers.length) {
            System.out.println("all servers are down");
            return null;
        }

        return picked;
    }

}
