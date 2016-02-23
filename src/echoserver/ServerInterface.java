/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 *
 * @author Jonathan Henriksen
 */
public interface ServerInterface {
    public void runServer(String host, int port) throws IOException;
    public void stopServer();
    public void addClient(String client, PrintWriter out);
    public void removeClient(String client);
    public void addMessage(Message message);
    public Map<String, PrintWriter> getClients();
    
    
}
