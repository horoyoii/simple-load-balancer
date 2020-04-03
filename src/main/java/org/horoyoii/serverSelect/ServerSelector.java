package org.horoyoii.serverSelect;

import org.horoyoii.model.Info;
import java.util.ArrayList;

public interface ServerSelector{

    
    public Info getServer(ArrayList<Info> serverList);
}
