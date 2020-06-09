package org.horoyoii.algo;

import java.util.ArrayList;
import java.net.InetAddress;
import java.util.List;

import org.horoyoii.model.Peer;

public class RoundRobin implements Algo {
    private int curPosition = -1;
  
    @Override
    public Peer getPeer(List<Peer> serverList, InetAddress clientIp){
        curPosition = (curPosition + 1) % serverList.size(); 
        
        return serverList.get(curPosition);
    }

}
