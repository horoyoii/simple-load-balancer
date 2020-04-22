package org.horoyoii.serverSelect;

import org.horoyoii.model.Peer;
import java.util.ArrayList;
import java.net.InetAddress;

public interface ServerSelector{

    
    public Peer getServer(ArrayList<Peer> serverList, InetAddress clientIp);
}
