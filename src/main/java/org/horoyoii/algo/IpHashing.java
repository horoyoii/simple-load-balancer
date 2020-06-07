package org.horoyoii.algo;

import java.net.InetAddress;
import java.util.ArrayList;

import org.horoyoii.model.Peer;

public class IpHashing implements ServerSelector{

    @Override
    public Peer getPeer(ArrayList<Peer> serverList, InetAddress clientIp){
        int hash = clientIp.hashCode() % serverList.size();
        System.out.println(clientIp);
        System.out.println(clientIp.hashCode());
                
        return serverList.get(hash);
    }

}
