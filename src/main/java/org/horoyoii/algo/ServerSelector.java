package org.horoyoii.algo;

import org.horoyoii.model.Peer;
import java.util.ArrayList;
import java.net.InetAddress;

public interface ServerSelector{

    
    public Peer getPeer(ArrayList<Peer> serverList, InetAddress clientIp);
}
