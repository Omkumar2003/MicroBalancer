package pkg.config;

// import pkg.domain.Service;
import pkg.domain.Server;
import pkg.strategy.Rb;
import pkg.health.Checker;

public class ServerList{
   public  Server[] servers;
   public String Names;
   public  Rb strategy;
   public  Checker healthChecker;

    public ServerList(Server[] servers, String Names, Rb strategy, Checker healthChecker) {
        this.servers = servers;
        this.Names = Names;
        this.strategy = strategy;
        this.healthChecker = healthChecker;
    }

   
}
