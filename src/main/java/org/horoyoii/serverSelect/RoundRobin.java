package org.horoyoii.serverSelect;

import java.util.ArrayList;
import org.horoyoii.model.Peer;

public class RoundRobin implements ServerSelector{
    private int curPosition = -1;
  
    @Override
    public Peer getServer(ArrayList<Peer> serverList){ 
        curPosition = (curPosition + 1) % serverList.size(); 
        
        return serverList.get(curPosition);
    }

}
