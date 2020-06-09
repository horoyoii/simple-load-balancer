package org.horoyoii.algo;

import org.horoyoii.model.Peer;
import java.util.ArrayList;
import java.net.InetAddress;
import java.util.List;

public interface Algo {

    
    Peer getPeer(List<Peer> serverList, InetAddress clientIp);
}
