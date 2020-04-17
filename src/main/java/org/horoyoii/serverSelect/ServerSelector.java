package org.horoyoii.serverSelect;

import org.horoyoii.model.Peer;
import java.util.ArrayList;

public interface ServerSelector{

    
    public Peer getServer(ArrayList<Peer> serverList);
}
