package com.github.omkumargithub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

// Load balancer will use HTTP connexion because HTTP is TCP connexion not UDP connexion

// itna bada code hai nhi ...........just bcoz of runtime exception it has become too big

// well using javax.servlet ......gives lot of problem rather than solution ........bcoz servlet made for client server vut we want server to server 
public class TestMain {
    public static void main(String[] args) {
        int port = 8085;
        // handleClient humein io exception dega in thread .......so basically it is a
        // wrapper of a wrapper
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            while (true) {// infinte listen

                Socket clientSocket = serverSocket.accept();
                // why accept? bcoz it is a matcher It matches all the requests came from
                // different clients to the load balancer server different ports
                System.out.println("client connected......... " + clientSocket);

                // handleClient function will give io exception .....
                Thread thread = new Thread(() -> {
                    try {
                        handleClient(clientSocket);
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

    private static void handleClient(Socket clientSocket) throws IOException {
        // System.out.println(clientSocket);
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String request = in.readLine();
            System.out.println("Request received................ " + request);

            OutputStream out = clientSocket.getOutputStream();
            String responseBody = "response from the load balancer";

            // this is copied .......bcoz i am not learnning this syntax
            String temp = "HTTP/1.1 200 OK\r\n"
                    + "Content-Length: " + responseBody.getBytes().length + "\r\n"
                    + "Content-Type: text/plain\r\n"
                    + "\r\n"
                    + responseBody;

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

}

