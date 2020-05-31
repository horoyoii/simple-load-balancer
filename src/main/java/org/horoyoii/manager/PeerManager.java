package org.horoyoii.manager;

import java.util.ArrayList;
import java.net.InetAddress;
import org.horoyoii.serverSelect.*;
import org.horoyoii.model.Peer;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class PeerManager{
    ArrayList<Peer> peerList = new ArrayList<Peer>();
    
    /**
        default object is RR.
    */
    ServerSelector peerSelector = new RoundRobin();  
  
        
    /**
        Add a new server metadata.
    */
    public void add(String name, String ip, String port){
        peerList.add(new Peer(name, ip, Integer.parseInt(port)));
    }

    
    public void add(String name, String ip, String port, String weight){
        peerList.add(new Peer(name, ip, Integer.parseInt(port), Integer.parseInt(weight)));
    }
    

    /**
        Set a method to select servers.

    */
    public void setServerSelector(ServerSelector serv){    
        this.peerSelector = serv;
    }


    /**
        Retreive the peer which is selected based on algo.    
        
        + increase the number of connection of the peer by 1.
    */
    public Peer getPeer(InetAddress clientIp){
        Peer peer = peerSelector.getPeer(peerList, clientIp);
        peer.increaseConnectionCount();
         
        return peer;
    }

    
    //TODO : RENAME
    public void decreaseCount(Peer peer){
        peer.decreaseConnectionCount();
    }


    public void showList(){
        log.info("backend server list : ");
        log.info("------------------------------------------------");
        log.info("name          IP              PORT      WEIGHT");
        for(Peer info : peerList){
            log.info("{}          {}      {}        {}",info.getName(), info.getIp(), info.getPort(), info.getWeight());
        }        
        log.info("------------------------------------------------");
    }
}

