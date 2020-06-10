package org.horoyoii.algo;

import org.horoyoii.exception.AlgoNotValidException;

public class AlgoFactory {

    public static Algo getAlgo(String name) throws AlgoNotValidException{
        Algo algo = null;
        name = name.toLowerCase();

        //TODO : refactoring; this is too ugly...
        if(name.equals(AlgoName.RR.getName())){
            algo = new RoundRobin();
        }else if(name.equals(AlgoName.WRR.getName()))
            algo = new WeightedRoundRobin();
        else if(name.equals(AlgoName.IP_HASH.getName()))
            algo = new IpHashing();
        else if(name.equals(AlgoName.LEAST_CONN.getName()))
            algo = new LeastConn();
        else
            throw new AlgoNotValidException(name + " is not valid algorithm name.");


//        switch(name.toLowerCase()){
//            case AlgoName.RR.getName():
//                algo = new RoundRobin();
//                break;
//            case "ip_hash":
//                algo = new IpHashing();
//                break;
//            case "static_rr":
//                algo = new WeightedRoundRobin();
//                break;
//            case "least_conn":
//                algo = new LeastConn();
//                break;
//            default:
//                throw new AlgoNotValidException(name + "is not valid algorithm name.");
//        }

        return algo;
    }
}
