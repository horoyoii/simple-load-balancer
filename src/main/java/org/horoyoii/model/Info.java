package org.horoyoii.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Info{
    private String  name;
    private String  ip;
    private int     port;
    private int     weight;
        
    public Info(String name, String ip, int port){
        this.ip     = ip;
        this.port   = port;
        this.name   = name;
    }

    public Info(String name, String ip, int port, int weight){
        this(name, ip, port);
        this.weight = weight;
    }   
    

}
