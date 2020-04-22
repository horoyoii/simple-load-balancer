package org.horoyoii.serverSelect;

import java.util.ArrayList;
import java.net.InetAddress;

import org.horoyoii.model.Peer;


/**
    Simple 'for loop' is used to find the lesat connected upstream server.

    In the NGINX source codes, it uses the linked list for least connection method.
*/
public class LeastConn implements ServerSelector{
  
    @Override
    public Peer getServer(ArrayList<Peer> serverList, InetAddress clientIp){ 
       
        return new Peer();
    }

}
