package org.horoyoii.serverSelect;

import java.util.ArrayList;
import org.horoyoii.model.Info;

public class RoundRobin implements ServerSelector{
    private int curPosition = -1;
  
    @Override
    public Info getServer(ArrayList<Info> serverList){ 
        curPosition = (curPosition + 1) % serverList.size(); 
        
        return serverList.get(curPosition);
    }

}
