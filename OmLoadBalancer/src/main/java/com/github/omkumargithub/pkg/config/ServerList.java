package com.github.omkumargithub.pkg.config;


// import pkg.domain.Service;
// import pkg.domain.Server;
// import com.github.omkumargithub.pkg.domain.Service;
// import pkg.strategy.Istrategy
import com.github.omkumargithub.pkg.strategy.Istrategy;

import com.github.omkumargithub.pkg.health.Checker;
import com.github.omkumargithub.pkg.domain.Server;
import java.util.*;

public class ServerList{
    ArrayList<Server> servers = new ArrayList<>();
   public String Names;
   public Istrategy  strategy;
   public  Checker healthChecker;

    public ServerList(ArrayList<Server> servers, String Names, Istrategy strategy, Checker healthChecker) {
        this.servers = servers;
        this.Names = Names;
        this.strategy = strategy;
        this.healthChecker = healthChecker;
    }

   
}
