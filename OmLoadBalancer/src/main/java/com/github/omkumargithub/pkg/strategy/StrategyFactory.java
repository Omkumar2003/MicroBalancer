package com.github.omkumargithub.pkg.strategy;

import java.util.List;

import com.github.omkumargithub.pkg.domain.Server;

public class StrategyFactory {
    private final Istrategy strategy;

    public StrategyFactory(String strat) {
        switch (strat) {
            case "RoundRobin":
                this.strategy = new RoundRobin();
                break;
            case "WeightedRoundRobin":
                this.strategy = new WeightedRoundRobin();
                break;
            default:
                throw new IllegalArgumentException("Unknown strategy: " + strat);
        }
    }

    public Istrategy getStrategy() {
        return this.strategy;
    }
}
