package org.horoyoii.manager;

import java.util.ArrayList;
import java.net.InetAddress;
import org.horoyoii.serverSelect.*;
import org.horoyoii.model.Peer;
import org.horoyoii.util.ClassNameMapper;
import org.horoyoii.exception.*;

import lombok.extern.slf4j.Slf4j;


/**
    Responsibility 
    
    - Manage peers
    - Give a selected upstream server(peer) for LoadBalancer.
*/
@Slf4j
public class PeerManager{

    public static final String        DEFAULT_WEIGHT  = "1";
    private ArrayList<Peer>        peerList        = new ArrayList<Peer>();
    

    /**
     *    default object is RR.
     */
    ServerSelector peerSelector = new RoundRobin();  
  
        
    /**
     *   Add a new server metadata.
     */
    public void add(String name, String ip, String port){
        peerList.add(new Peer(name, ip, Integer.parseInt(port)));
    }

    
    public void add(String name, String ip, String port, String weight){
        peerList.add(new Peer(name, ip, Integer.parseInt(port), Integer.parseInt(weight)));
    }
    

    /**
     *   Set a method to select servers.
     */
    public void setServerSelector(String alias) throws Exception{    
        
        this.peerSelector = getServerSelectorFromAlias(alias);
    }


    /**
     *   Retreive the peer which is selected based on algo.    
     *   
     *   + increase the number of connection of the peer by 1.
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




    public ServerSelector getServerSelectorFromAlias(String classAlias) throws Exception{
        ServerSelector ss = null;

        try{
            ss =(ServerSelector)Class.forName(ClassNameMapper.getClassName(classAlias))
                                                    .getDeclaredConstructor().newInstance();
        }catch(ClassNotFoundException e){
            throw new AlgoNotValidException(classAlias+" is not valid algorithm name");
        }

        return ss;
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

