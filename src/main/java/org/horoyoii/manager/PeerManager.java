package org.horoyoii.manager;

import java.util.ArrayList;
import java.net.InetAddress;
import java.util.List;

import org.horoyoii.algo.*;
import org.horoyoii.model.Peer;
import org.horoyoii.utils.ClassNameMapper;
import org.horoyoii.exception.*;

import lombok.extern.slf4j.Slf4j;


/**
    Responsibility 
    
    - Manage peers
    - Give a selected upstream server(peer) for LoadBalancer.
*/
@Slf4j
public class PeerManager{

    public static final String      DEFAULT_WEIGHT  = "1";
    private List<Peer>              peerList        = new ArrayList<Peer>();
    

    /**
     *    default algorithm is RR.
     */
    Algo algo = new RoundRobin();
  
        
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
     *   Set a algorithm to select servers.
     */
    public void setServerSelector(String alias) throws Exception{
        this.algo = getServerSelectorFromAlias(alias);
    }


    /**
     *   Retreive the peer which is selected based on algo.    
     *   
     *   + increase the number of connection of the peer by 1.
     */
    public synchronized Peer getPeer(InetAddress clientIp){
        Peer peer = algo.getPeer(peerList, clientIp);

        peer.increaseConnectionCount();
        log.debug("opened : current connection cnt : "+peer.getConns());
        return peer;
    }


    public synchronized void releasePeer(Peer peer){
        decreaseCount(peer);
        log.debug("closed : current connection cnt : "+peer.getConns());
    }


    //TODO : RENAME
    private void decreaseCount(Peer peer){
        peer.decreaseConnectionCount();
    }



    public Algo getServerSelectorFromAlias(String classAlias) throws Exception{
        Algo ss = null;

        try{
            ss =(Algo)Class.forName(ClassNameMapper.getClassName(classAlias))
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

