package org.horoyoii.algo;

public enum AlgoName {

    RR("round_robin"),

    WRR("static_rr"),

    IP_HASH("ip_hash"),

    LEAST_CONN("least_conn");


    private String name;

    AlgoName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

}
