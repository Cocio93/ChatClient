/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jonathan Henriksen
 */
public class ClientThread implements Runnable {
    private static final String LOG_FILE = "serverlog.txt";
    private static final String LOG_NAME = "errorlog";

    private ServerInterface server;
    private final Socket socket;
    private final PrintWriter out;
    private final Scanner in;
    private final String ip;
    private String username = null;

    public ClientThread(Socket socket, ServerInterface server) throws IOException {
        this.server = server;
        this.socket = socket;
        ip = socket.getRemoteSocketAddress().toString();
        in = new Scanner(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        while (true) {
            if (in.hasNext()) {
                String[] splitMessage = in.nextLine().split("#");
                String tag = splitMessage[0];
                for (String str : splitMessage) {
                }
                switch (tag) {
                    case "SEND":
                        System.out.println("Received message - " + splitMessage[2] + " - from " + username);
                        Logger.getLogger(Log.LOG_NAME).log(Level.INFO, "Received message - " + splitMessage[2] + " - from " + username);
                        String receiver = splitMessage[1];
                        String message = splitMessage[2];
                        ArrayList<String> receivers = new ArrayList() {};    
                        if (receiver.contains(",")) {
                            String[] split = receiver.split(",");
                            for (String rec : split) {
                                receivers.add(rec);
                            }
                        } else {
                            receivers.add(splitMessage[1]);
                        }
                        server.addMessage(new Message(message, username, receivers));
                        break;

                    case "USER":
                        if (username == null && splitMessage.length == 2) {
                            username = splitMessage[1];
                            server.addClient(username, out);
                            System.out.println("Client confirmed name - " + username);
                            Logger.getLogger(Log.LOG_NAME).log(Level.INFO, "User - '" + username + "' joined the server");
                        } else {
                            System.err.println(username + "send invalid request");
                            out.println("Error in chat protocol!");
                        }
                        break;

                    case "LOGOUT":
                        System.out.println(username + " left");
                        out.println("Byyee, have a beautiful time!");
                        server.removeClient(username);
                        Logger.getLogger(Log.LOG_NAME).log(Level.INFO, "Removed user - '" + username + "'. Reason: Send LOGOUT");
                         {
                            try {
                                socket.close();
                            } catch (IOException ex) {
                                Logger.getLogger(Log.LOG_NAME).log(Level.SEVERE, ex.getMessage());
                            }
                        }
                        break;

                    default:
                        System.err.println("Error in chat protocol!");
                        out.println("Protocol error");
                        break;
                }
            }
        }
    }
}
