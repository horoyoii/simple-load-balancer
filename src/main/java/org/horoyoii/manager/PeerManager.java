package org.horoyoii.manager;

import java.util.ArrayList;
import java.net.InetAddress;
import java.util.List;

import org.horoyoii.algo.*;
import org.horoyoii.exception.AlgoNotValidException;
import org.horoyoii.exception.NoLiveUpstreamException;
import org.horoyoii.model.Peer;

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
     *  Add a upstream server information to the List.
     *
     * @param name
     * @param ip
     * @param port
     * @param weight
     */
    public void add(String name, String ip, String port, String weight){
        peerList.add(new Peer(name, ip, Integer.parseInt(port), Integer.parseInt(weight)));
    }


    /**
     *   Set a algorithm for LB.
     */
    public void setLoadBalancingAlgorithm(String name) throws AlgoNotValidException {
        this.algo = AlgoFactory.getAlgo(name);
    }


    /**
     *   Retrieve the peer which is selected based on algo.
     *
     *   + increase the number of connection of the peer by 1.
     *   @Return
     *
     */
    public synchronized Peer getPeer(InetAddress clientIp) throws NoLiveUpstreamException {
        Peer peer = null; //algo.getPeer(peerList, clientIp);

        if(peer == null)
            throw new NoLiveUpstreamException("no live upstreams while connecting to upstream");

        peer.increaseConnectionCount();
        return peer;
    }



    public synchronized void releasePeer(Peer peer){
        decreaseCount(peer);
    }



    //TODO : RENAME
    private void decreaseCount(Peer peer){
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

