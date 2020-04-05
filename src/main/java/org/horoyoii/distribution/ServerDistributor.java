package org.horoyoii.distribution;

import java.util.ArrayList;
import org.horoyoii.serverSelect.*;
import org.horoyoii.model.Info;


public class ServerDistributor{
    ArrayList<Info> serverList = new ArrayList<Info>();
    
    ServerSelector serverSelector;
  
        
    /*
        Add a new server metadata.
    */
    public void add(String name, String ip, String port){
        serverList.add(new Info(name, ip, Integer.parseInt(port)));
    }
    

    public void setServerSelector(ServerSelector serv){
        this.serverSelector = serv;
    }


    /*
        TODO: Synchronization
    */
    public Info getSelectedServer(){
        return serverSelector.getServer(serverList);
    }


    public void showList(){
        for(Info info : serverList){
            System.out.println(info);            
        }
    }
}

