package org.horoyoii.algo;

import java.util.ArrayList;
import java.net.InetAddress;

import org.horoyoii.model.Peer;


/**
    Least Connected Algorithm considers not only the number of connection but also the weight of the server.
    Simple 'for loop' is used to find the lesat connected upstream server.

    In the NGINX source codes, it uses the linked list for least connection method.
    
*/
public class LeastConn implements ServerSelector{
  
    @Override
    public Peer getPeer(ArrayList<Peer> peers, InetAddress clientIp){ 
        Peer best = null;

        for(Peer peer : peers){
            
            if(best == null || peer.getConns() * best.getWeight() < best.getConns() * peer.getWeight()){
                best = peer;
            } 
        }
                
        return best;
    }

}
