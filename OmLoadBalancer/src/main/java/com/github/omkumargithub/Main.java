package com.github.omkumargithub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.net.URL;
import java.net.HttpURLConnection;

import com.github.omkumargithub.pkg.config.Config;
// import com.github.omkumargithub.pkg.domain.Service;
import com.github.omkumargithub.pkg.domain.Server;
import com.github.omkumargithub.helper.ReverseProxy;
import com.github.omkumargithub.pkg.health.Checker;
import com.github.omkumargithub.pkg.config.ServerList;
import com.github.omkumargithub.pkg.strategy.StrategyFactory;

class Ok {
    Config config;
    HashMap<String, ServerList> omServerList;

    public Ok(Config conf) {
        // the main purpose of this ServerMap is tht we can check health
        HashMap<String, ServerList> ServerMap = new HashMap<>();

        for (int i = 0; i < conf.services.size(); i++) {

            final int outIndex = i;

            // ArrayList<Server> servers = new ArrayList<>();

            List<Server> servers = Collections.synchronizedList(new ArrayList<>());

            for (int j = 0; j < conf.services.get(i).replicas.size(); j++) {
                final int inIndex = j;

                String ur = conf.services.get(i).replicas.get(j).url;
                // making proxy

                // ***************************************************************

                Thread th2 = new Thread(() -> {
                    try {
                        int lastIndex = ur.lastIndexOf(':');
                        int port = Integer.parseInt(ur.substring(lastIndex + 1));

                        ServerSocket serverSocket = new ServerSocket(port);

                        // ReverseProxy rp = new ReverseProxy();
                        servers.add(new Server(ur, serverSocket,
                                conf.services.get(outIndex).replicas.get(inIndex).metaData));
                        System.out.println("Target Server started on port " + port);

                        while (true) {// infinte listen

                            Socket clientSocket = serverSocket.accept();
                            // why accept? bcoz it is a matcher It matches all the requests came from
                            // different clients to the load balancer server different ports
                            // System.out.println("client connected on target Server......... " +
                            // clientSocket);

                            // handleClient function will give io exception .....
                            Thread thread = new Thread(() -> {
                                try {
                                    synchronized (servers) {
                                        for (Server server : servers) {
                                            if (ur == server.url) {
                                                server.handleClient(clientSocket);
                                            }
                                            // System.out.println(server.getName());
                                        }
                                    }

                                    // handleClient(clientSocket);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                            thread.start();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                th2.start();

                /******************************************************************** */
                //
            }

            Checker newChecker = new Checker(servers);

            ServerMap.put(conf.services.get(i).matcher, new ServerList(
                    servers,
                    conf.services.get(i).name,
                    new StrategyFactory(conf.services.get(i).strategy).getStrategy(),
                    newChecker));
        }

        ServerMap.forEach((k, v) -> {
            v.healthChecker.start();
        });

        this.config = conf;
        this.omServerList = ServerMap;
    }

    // it is notreturning full serverlist ....it is just a name for omserverlist
    // data's value type

    public ServerList findServiceList(String reqPath) {
        ServerList temp = null;

        for (Map.Entry<String, ServerList> entry : omServerList.entrySet()) {
            if (reqPath.startsWith(entry.getKey())) {
                System.out.println("url found");
                temp = entry.getValue();
            }
        }
        return temp;
    }

    public void serveHttp(Socket clientSocket) throws IOException {
        String LoadBalancerToTargetServers = LoadBalancerToTargetServers();

        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String request = in.readLine();
            System.out.println("Request received................ " + request);

            OutputStream out = clientSocket.getOutputStream();

            // this is copied .......bcoz i am not learnning this syntax
            String temp = "HTTP/1.1 200 OK\r\n"
                    + "Content-Length: " + LoadBalancerToTargetServers.getBytes().length + "\r\n"
                    + "Content-Type: text/plain\r\n"
                    + "\r\n"
                    + LoadBalancerToTargetServers;

            out.write(temp.getBytes());
            out.flush();
            out.close();
            in.close();
            // seeee........For the specific food the connexion is closed
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String LoadBalancerToTargetServers() {
        ServerList sl = findServiceList("/");
        Server s = sl.strategy.next(sl.servers);
        String temp = "";
        // logic for target service hitLer
        try {
            URL url = new URL(s.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            temp = response.toString();
            in.close();
        } catch (Exception e) {

        }

        return temp;
    }

}

public class Main {
    public static void main(String[] args) {
        int port = 3000;
        String filePath = "OmLoadBalancer\\src\\main\\resouces\\wConfig.yaml";
    
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Load Balancer started on port " + port);
    
            // Using volatile to ensure visibility across threads
            volatile Ok ok = null;
    
            // Start a thread to initialize Ok object
            Thread okThread = new Thread(() -> {
                Config config = Config.loadConfigFromFile(filePath);
                ok = new Ok(config);
            });
            okThread.start();
    
            // Wait for Ok initialization to complete
            synchronized (okThread) {
                while (ok == null) {
                    try {
                        okThread.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
    
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected on load balancer: " + clientSocket);
    
                Thread thread = new Thread(() -> {
                    try {
                        System.out.println("Client connected on target server: " + clientSocket);
                        ok.serveHttp(clientSocket);
    
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
