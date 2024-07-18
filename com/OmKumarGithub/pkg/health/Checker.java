package pkg.health;

import pkg.domain.Server;
import java.net.UnknownHostException;
import java.net.Socket;
import java.io.IOException;
import pkg.domain.Server;
import java.util.*;;

public class Checker {
    ArrayList<Server> servers = new ArrayList<Server>();
    int period;

    public Checker(ArrayList<Server> servers){

        this.servers = servers;

    }

    public void start() {
        System.out.println("starting............");
        while (true) {
            for (int i = 0; i < servers.size(); i++) {
                final int serverIndex = i;
                // We are not able to access I inside the thread that's why we are declaring this here
                
                Thread T = new Thread(() -> {
                    checkHealth(servers.get(serverIndex)); 
                    // Just try to access some index it will not work we are not equal to access outside variable until its final
                    // I think it is done so that there can be a consistency in the values 
                });
                T.start();
            }
            
            try {
                Thread.sleep(10000);
            } catch (Exception e) {
                // inform admin if there is no admin then u r offically cooked
            }
        }

    }

    public void checkHealth(Server server) {
        String serverName = "localhost";
        // it is not taking server.url
        // idk right now why

        int port = 12345;
        try {
            // boolean isAlive = server.isAlive();
            Socket socket = new Socket(serverName, port);
            // phele ka server check krna hai .....if needed we should update it 
            if(!server.isAlive()){
                server.setLiveness(true);
                System.out.println("dead came to life");
            }else{
            server.setLiveness(true);
                System.out.println("life is going well");
            }
            socket.close();

        } catch (UnknownHostException e) {
            server.setLiveness(false);
            System.out.println("RIP.......");

        } catch (IOException e) {
            server.setLiveness(false);
            System.out.println("RIP.......");
        }

    }
}
