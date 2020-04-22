package org.horoyoii.util;

import java.util.HashMap;

public class ClassNameMapper{
    
    final static String                 path    = "org.horoyoii.serverSelect.";
    static HashMap<String, String>      info;

    static{
        info = new HashMap<>();
        
        info.put("roundrobin", "RoundRobin");
        info.put("static-rr", "WeightedRoundRobin");
        info.put("ip-hashing", "IpHashing");
        info.put("leastconn", "LeastConn");
    }


    public static String getClassName(String alias){
        return path+info.get(alias);
    }

    private ClassNameMapper(){}
}
