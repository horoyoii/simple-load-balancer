package org.horoyoii.distribution;

import java.util.ArrayList;
import lombok.Getter;

public class ServerDistributor{
    ArrayList<Info> serverList;

    
     
}

@Getter
class Info{
    private String  ip;
    private int     port;
    private String  name;
    
    Info(String ip, int port, String name){
        this.ip     = ip;
        this.port   = port;
        this.name   = name;
    }

    

}
