package org.horoyoii.algo;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.horoyoii.model.Peer;

public class IpHashing implements Algo {

    @Override
    public Peer getPeer(List<Peer> serverList, InetAddress clientIp){
        int hash = clientIp.hashCode() % serverList.size();
        return serverList.get(hash);
    }

}
