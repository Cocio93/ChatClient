package echoserver;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EchoServer implements ServerInterface {
    private static final String LOG_FILE = "serverlog.txt";
    private static final String LOG_NAME = "errorlog";
    private static final Map<String, PrintWriter> clients = new HashMap();
    private List<Message> messages = Collections.synchronizedList(new ArrayList());
    private static int port;
    private static String host = "localhost";
    private  ServerSocket serverSocket;
    private static Socket clientSocket;

    public static void main(String[] args) throws IOException {
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 9090;
        }
        Log.setLogFile("logFile.txt", "ServerLog");
        new EchoServer().runServer(host, port);

    }

    @Override
    public void runServer(String host, int port) throws IOException {
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(host, port));
            System.out.println("Server is running ");
            Logger.getLogger(Log.LOG_NAME).log(Level.INFO, "Server started. Listening on: " + port + ", bound to " + host);
            threadStreamer(new Streamer(messages, this));
            while (true) {
                System.out.println("Listening for clients...");
                clientSocket = serverSocket.accept();
                threadSocket(clientSocket);
                System.out.println("Client connected from" + clientSocket.getRemoteSocketAddress());
            }
        } catch (IOException ex) {
            System.err.println(ex);
            System.exit(1);
        }
    }

    @Override
    public void stopServer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void threadSocket(Socket socket) throws IOException {
        Thread thread = new Thread(new ClientThread(socket, this));
        thread.start();
    }
    
    private void threadStreamer(Streamer streamer) {
        Thread thread = new Thread(streamer);
        thread.start();
    }
    
    @Override
    public void addClient(String client, PrintWriter out) {
        clients.put(client, out);
        sendUsers();
    }
    
    @Override
    public void removeClient(String client) {
        clients.get(client).close();
        clients.remove(client);
        sendUsers();
    }
    @Override
    public void addMessage(Message message) {
        messages.add(message);
    }
    
    @Override
    public Map<String, PrintWriter> getClients() {
        return clients;
    }
    
    public void sendUsers() {
        String tag = "USERS#";
        String online = "";
        for (Map.Entry<String, PrintWriter> set : getClients().entrySet()) {
            online += set.getKey() + ",";
        }
        online = online.substring(0, tag.length()-1);
        for (Map.Entry<String, PrintWriter> set : getClients().entrySet()) {
            set.getValue().println(tag + online);
        }
    }


}
