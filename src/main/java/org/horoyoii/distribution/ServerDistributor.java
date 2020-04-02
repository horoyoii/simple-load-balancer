package org.horoyoii.distribution;

import java.util.ArrayList;
import lombok.Getter;
import lombok.ToString;


public class ServerDistributor{
    ArrayList<Info> serverList = new ArrayList<Info>();

    
    /*
        Add a new server metadata.
    */
    public void add(String name, String ip, String port){
        serverList.add(new Info(name, ip, Integer.parseInt(port)));
    }
     
    


    public void showList(){
        for(Info info : serverList){
            System.out.println(info);            
        }
    }
}

@Getter
@ToString
class Info{
    private String  name;
    private String  ip;
    private int     port;
    
    Info(String name, String ip, int port){
        this.ip     = ip;
        this.port   = port;
        this.name   = name;
    }

    

}
