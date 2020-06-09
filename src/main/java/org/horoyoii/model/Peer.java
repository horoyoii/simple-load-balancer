package org.horoyoii.model;

import lombok.Getter;
import lombok.ToString;
import lombok.NoArgsConstructor;

@Getter
@ToString
@NoArgsConstructor
public class Peer{
    private String  name;
    private String  ip;
    private int     port;
    private int     weight = 1;
    private int     conns  = 0;         // the number of current connection to this peer.    


    private boolean down;


    public Peer(String name, String ip, int port){
        this.ip     = ip;
        this.port   = port;
        this.name   = name;
    }


    public Peer(String name, String ip, int port, int weight){
        this(name, ip, port);
        this.weight = weight;
    }   

    public void increaseConnectionCount(){
        conns += 1;   
    }

    public void decreaseConnectionCount(){
        conns -= 1;
    }

}
