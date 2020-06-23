package org.horoyoii.model;

import lombok.Getter;
import lombok.ToString;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
public class Peer{

    private String  name;
    private String  ip;
    private int     port;

    private int     weight = 1;
    private int     conns  = 0;         // the number of current connection to this peer.    

    private LocalDateTime accessed;
    private LocalDateTime checked;

    private int     fail_timeout = 1;   // 다시 health check 해보기까지의 시간차이

    private boolean down;

    private long    tot = 0;


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
        tot++;
    }


    public void decreaseConnectionCount(){
        conns -= 1;
    }

}
