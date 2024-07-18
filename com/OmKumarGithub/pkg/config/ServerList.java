package pkg.config;

// import pkg.domain.Service;
import pkg.domain.Server;
import pkg.strategy.Rb;
import pkg.health.Checker;
import pkg.domain.Server;
import java.util.*;

public class ServerList{
    ArrayList<Server> servers = new ArrayList<Server>();
   public String Names;
   public  Rb strategy;
   public  Checker healthChecker;

    public ServerList(ArrayList<Server> servers, String Names, Rb strategy, Checker healthChecker) {
        this.servers = servers;
        this.Names = Names;
        this.strategy = strategy;
        this.healthChecker = healthChecker;
    }

   
}
