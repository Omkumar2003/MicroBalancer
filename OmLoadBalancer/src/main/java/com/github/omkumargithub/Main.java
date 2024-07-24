package com.github.omkumargithub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.github.omkumargithub.pkg.config.Config;
import com.github.omkumargithub.pkg.config.ServerList;
import com.github.omkumargithub.pkg.domain.Server;
import com.github.omkumargithub.pkg.health.Checker;
import com.github.omkumargithub.pkg.strategy.StrategyFactory;

class Ok {
    Config config;
    HashMap<String, ServerList> omServerList;

    public HashMap<String, ServerList> giveServerMap(Config conf) {
        HashMap<String, ServerList> ServerMap = new HashMap<>();

        for (int i = 0; i < conf.services.size(); i++) {
            final int outIndex = i;
            // List<Server> servers = Collections.synchronizedList(new ArrayList<>());
            List<Server> servers = giveTargetServers(conf, outIndex);

            Checker newChecker = new Checker(servers);

            ServerMap.put(conf.services.get(i).matcher, new ServerList(
                    servers,
                    conf.services.get(i).name,
                    new StrategyFactory(conf.services.get(i).strategy).getStrategy(),
                    newChecker));
        }
        return ServerMap;

    }

    public int findPortFromUrl(String ur) {
        int lastIndex = ur.lastIndexOf(':');
        int port = Integer.parseInt(ur.substring(lastIndex + 1));
        return port;

    }

    public void infiniteTragetServerListen(List<Server> servers, ServerSocket serverSocket, String ur) {
        while (true) {// infinte listen
            try {
                Socket clientSocket = serverSocket.accept();
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
            } catch (Exception e) {

            }

        }

    }

    public List<Server> giveTargetServers(Config conf, int i) {
        // ******************i dont know .....this line can give bugs
        // List<Server> servers = Collections.synchronizedList(new ArrayList<>());
        List<Server> servers = new ArrayList<>();

        for (int j = 0; j < conf.services.get(i).replicas.size(); j++) {
            final int inIndex = j;
            String ur = conf.services.get(i).replicas.get(j).url;
            int port = findPortFromUrl(ur);

            // Thread th2 = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                servers.add(new Server(ur, serverSocket, conf.services.get(i).replicas.get(inIndex).metaData));
                System.out.println("Target Server started on port " + port);

                Thread th3 = new Thread(() -> {
                    infiniteTragetServerListen(servers, serverSocket, ur);
                });
                th3.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // });
            // th2.start();

        }
        return servers;
    }

    public Ok(Config conf) {
        // the main purpose of this ServerMap is tht we can check health
        HashMap<String, ServerList> ServerMap = giveServerMap(conf);

        // this funcking IDIOT FUNCTION WAS BLOCKING A CALL ......................I
        // SPENT 2
        // DAYS TO FIND THIS FUCKING PROBLEM
        Thread namingIsTough = new Thread(() -> {
            ServerMap.forEach((k, v) -> {
                v.healthChecker.start();
            });
        });
        namingIsTough.start();

        this.config = conf;
        this.omServerList = ServerMap;
    }

    // it is notreturning full serverlist ....it is just a name for omserverlist
    // data's value type

    public ServerList findServiceList(String reqPath) {
        ServerList temp = null;

        for (Map.Entry<String, ServerList> entry : omServerList.entrySet()) {
            if (reqPath.startsWith(entry.getKey())) {
                // System.out.println("url found");
                temp = entry.getValue();
            }
        }
        return temp;
    }

    public void serveHttp(Socket clientSocket) throws IOException {
        String reqResultOfLoadBalancerToTarget = reqResultOfLoadBalancerToTarget();
        System.out.println(reqResultOfLoadBalancerToTarget);

        try {
            // this req is from client

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            StringBuilder requestBuilder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                requestBuilder.append(line).append("\r\n");
            }

            // Append the last line which is empty (end of headers)
            requestBuilder.append("\r\n");

            String request = requestBuilder.toString();

            System.out.println("Request received By Load Balancer by client ................ \n" + request);

            OutputStream out = clientSocket.getOutputStream();

            // this is copied .......bcoz i am not learnning this syntax
            String temp = "HTTP/1.1 200 OK\r\n"
                    + "Content-Length: " + reqResultOfLoadBalancerToTarget.getBytes().length + "\r\n"
                    + "Content-Type: text/plain\r\n"
                    + "\r\n"
                    + reqResultOfLoadBalancerToTarget;

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

    public String reqResultOfLoadBalancerToTarget() {
        ServerList sl = findServiceList("/");
        System.out.println("Choosing strategy accordindg to the startergy given");

        Server s = sl.strategy.next(sl.servers);
        String temp = "";
        // logic for target service hitLer
        try {
            System.out.println("Strategy Decided to use this Relpica    " + s.url);

            URL url = new URL(s.url);
            // URL url = new URL("http://127.0.0.1:8082");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader ResultFromTargetServer = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String ResultLine;
            StringBuilder resplonseGivenByTargetServer = new StringBuilder();
            while ((ResultLine = ResultFromTargetServer.readLine()) != null) {
                resplonseGivenByTargetServer.append(ResultLine);
            }
            temp = resplonseGivenByTargetServer.toString();
            ResultFromTargetServer.close();
        } catch (IOException e) {

        }

        return temp;
    }

}

public class Main {

    private static volatile Ok sharedOk;

    public static void setsharedOk(Ok value) {
        sharedOk = value;
    }

    public static Ok getsharedOk() {
        return sharedOk;
    }

    // I know this is the most idiotic thing a person can do.............But I'm not
    // able to understand how well the path actually works in Java
    public static String doublingSlash(String s) {
        String result = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '/') {
                result += "//";
            } else {
                result += s.charAt(i);
            }
        }
        return result;
    }

    public static void main(String[] args) {

        int port;

        try {

            // first start lOAD BAlancer server
            Scanner sc = new Scanner(System.in);
            System.out.println("Give the Load Balancer Port number if typed wrong default port 3000 will open");
            System.out.println("Port number Should be in the range of 8000 - 8100 ");
            try {
                int temp = Integer.parseInt(sc.nextLine());
                if (temp > 8000 && temp < 8100) {
                    port = temp;
                } else {
                    port = 3000;
                }
            } catch (Exception e) {
                port = 3000;
            }
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Load Balancer  started on port " + port);

            System.out.print("Provide ABSOLUTE path of YAML file :");

            // System.out.println("Current working directory: " +
            // System.getProperty("user.dir"));

            String Filepath = sc.nextLine().replace("\\", "\\\\");

            // then start target servers
            Thread th1 = new Thread(() -> {
                // String Filepath = "C:\\Users\\OM KUMAR\\Desktop\\New
                // folder\\OmLoadBalancer\\src\\main\\resouces\\wConfig2.yaml";
                Config config = Config.loadConfigFromFile(Filepath);
                setsharedOk(new Ok(config));

            });
            th1.start();

            Thread th = new Thread(() -> {

                try {
                    while (true) {// infinte listen
                        // serverSocket.accept(); it is a blocking call
                        Socket clientSocket = serverSocket.accept();
                        System.out.println(
                                "Client connected On load Balancer with a socket Object......... " + clientSocket);

                        // handleClient function will give io exception .....
                        Thread thread = new Thread(() -> {
                            try {
                                getsharedOk().serveHttp(clientSocket);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        thread.start();
                    }
                } catch (IOException e) {
                    System.out.println("Load balancening connection is not working ");
                }
            });
            try {
                th1.join();
            } catch (Exception e) {
                System.out.println("Current Thread:" + Thread.currentThread().getName());
            }
            th.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
