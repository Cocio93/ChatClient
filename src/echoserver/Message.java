
package echoserver;

import java.util.ArrayList;

public class Message {
    private ArrayList<String> rec;
    private String text, sender;
    
    public Message(String text, String sender, ArrayList<String> rec) {
       this.rec = rec;
       this.text = text;
       this.sender = sender;
    }

    public ArrayList<String> getRec() {
        return rec;
    }

    public void setRec(ArrayList<String> rec) {
        this.rec = rec;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
    
}
