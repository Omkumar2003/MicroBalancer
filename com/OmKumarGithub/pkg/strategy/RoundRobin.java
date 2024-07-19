package pkg.strategy;

import pkg.domain.Server;

public class RoundRobin {
    int current =0 ;

    Server next(Server[] servers){
      int  seen =0;
      Server picked = null;

        while(seen<servers.length){
          picked = servers[current];
          current = (current+1)% servers.length;
          if(picked.isAlive()){
            break;
          }
          seen++;
        }
          if(picked==null || seen == servers.length){
            System.out.println("all servers are down");
            return null;
          }

          return picked;
        }
    }

