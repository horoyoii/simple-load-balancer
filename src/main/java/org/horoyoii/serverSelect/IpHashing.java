package org.horoyoii.serverSelect;

import java.net.InetAddress;
import java.util.ArrayList;

import org.horoyoii.model.Info;

public class IpHashing implements ServerSelector{
    private InetAddress clientIp;

    @Override
    public Info getServer(ArrayList<Info> serverList){
        int hash = clientIp.hashCode() % serverList.size();
        
        return serverList.get(hash);
    }

    public void setClientIp(InetAddress clientIp){
        this.clientIp = clientIp;
    }

}
