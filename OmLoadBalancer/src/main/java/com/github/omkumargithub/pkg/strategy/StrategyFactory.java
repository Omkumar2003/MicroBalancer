package com.github.omkumargithub.pkg.strategy;

import java.util.List;

import com.github.omkumargithub.pkg.domain.Server;

public class StrategyFactory {

    public Server helper(String strat, List<Server> servers) {
        Server temp = null;
        switch (strat) {
            case "RR":
                // temp = RoundRobin.next(servers);
                break;
            case "WRR":
                // temp = WeigthedRoundRobin.next(servers);
                break;
        }
        return temp;
    }
}
