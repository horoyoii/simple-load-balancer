package org.horoyoii.distribution;

import java.util.ArrayList;
import java.net.InetAddress;
import org.horoyoii.serverSelect.*;
import org.horoyoii.model.Peer;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ServerDistributor{
    ArrayList<Peer> serverList = new ArrayList<Peer>();
    
    /**
        default object is RR.
    */
    ServerSelector serverSelector = new RoundRobin();  
  
        
    /**
        Add a new server metadata.
    */
    public void add(String name, String ip, String port){
        serverList.add(new Peer(name, ip, Integer.parseInt(port)));
    }

    
    public void add(String name, String ip, String port, String weight){
        serverList.add(new Peer(name, ip, Integer.parseInt(port), Integer.parseInt(weight)));
    }
    

    /**
        Set a method to select servers.

    */
    public void setServerSelector(ServerSelector serv){
        this.serverSelector = serv;
    }


    /**
        
    */
    public Peer getSelectedServer(InetAddress clientIp){
       return serverSelector.getServer(serverList, clientIp);
    }


    public void showList(){
        log.info("backend server list : ");
        log.info("------------------------------------------------");
        log.info("name          IP              PORT      WEIGHT");
        for(Peer info : serverList){
            log.info("{}          {}      {}        {}",info.getName(), info.getIp(), info.getPort(), info.getWeight());
        }        
        log.info("------------------------------------------------");
    }
}

