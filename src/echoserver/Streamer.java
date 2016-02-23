/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jonathan Henriksen
 */
public class Streamer implements Runnable{
    private List<Message> messages;
    private final ServerInterface server;
    
    public Streamer(List<Message> list, ServerInterface server) {
        messages = list;
        this.server = server;
        
    }

    @Override
    public void run() {
        while (true) {
            if (!messages.isEmpty()) {
                send(messages.remove(0));
            }
        }
    }

    private void send(Message msg) {
        ArrayList<String> rec = msg.getRec();
        List<PrintWriter> writers = new ArrayList<PrintWriter>();
        if (rec.get(0).equalsIgnoreCase("*")) {
            writers = getAllUsers();
        } else {
            writers = getUsers(msg.getRec());
        }
        for (PrintWriter pw : writers) {
            String message = "MESSAGE#" + msg.getSender() + "#" + msg.getText();
            System.out.println("Sending message - " + message);
            pw.println(message);
        }
        
    }
    
    private ArrayList<PrintWriter> getAllUsers() {
        ArrayList<PrintWriter> pws = new ArrayList();
        for (Map.Entry<String, PrintWriter> set : server.getClients().entrySet()) {
            pws.add(set.getValue());
            
        }
        return pws;      
    }
    
    private ArrayList<PrintWriter> getUsers(ArrayList<String> rec) {
        ArrayList<PrintWriter> pws = new ArrayList();
        for (String name : rec) {
            pws.add(server.getClients().get(name));
        }
        return pws;
    }
}
