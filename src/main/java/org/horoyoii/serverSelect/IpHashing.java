package org.horoyoii.serverSelect;

import java.net.InetAddress;
import java.util.ArrayList;

import org.horoyoii.model.Peer;

public class IpHashing implements ServerSelector{

    @Override
    public Peer getServer(ArrayList<Peer> serverList, InetAddress clientIp){
        int hash = clientIp.hashCode() % serverList.size();
        
        return serverList.get(hash);
    }

}
