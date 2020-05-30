package org.horoyoii.serverSelect;

import java.util.ArrayList;
import java.net.InetAddress;

import org.horoyoii.model.Peer;

public class RoundRobin implements ServerSelector{
    private int curPosition = -1;
  
    @Override
    public Peer getPeer(ArrayList<Peer> serverList, InetAddress clientIp){ 
        curPosition = (curPosition + 1) % serverList.size(); 
        
        return serverList.get(curPosition);
    }

}
