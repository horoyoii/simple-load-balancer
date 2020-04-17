package org.horoyoii.loadbalance;

import java.io.*;
import java.net.*;

import org.horoyoii.distribution.ServerDistributor;
import org.horoyoii.serverSelect.*;
import org.horoyoii.connection.Connection;
import org.horoyoii.model.Info;

import lombok.extern.slf4j.Slf4j;

/**
    Default 'server weights' is 1.


*/
@Slf4j
public class LoadBalancer{

    ServerDistributor   serverDistributor   = new ServerDistributor();
    ServerSocket        listenSock;     
    int                 port                = 9901;
           
    final String        DEFAULT_WEIGHT      = "1";
    Algorithm           algorithm           = Algorithm.ROUNDROBIN;    
    
    
    /*
        If there is no information about load balancing algorithm,
        then the default is RR.

    */
    public void readConfig(){
        log.info("read configuration...");
        String path = "/home/horoyoii/Desktop/simple-load-balancer/load.config";
        
        File f              = null;
        FileReader fr       = null;
        BufferedReader br   = null;
        
        boolean isDefault = true;

        try{
            fr = new FileReader(new File(path));
            br = new BufferedReader(fr);
 
            String line = br.readLine();
            while(line != null){
                
                if(line.length() != 0 && line.charAt(0) != '#'){ // ignore the comments(#)                     
                    String[] splits = line.split(" ");

                    if(splits[0].equals("balance")){

                        isDefault = false;
                        ServerSelector select = null;

                        switch( splits[1] ){
                            case "roundrobin" :
                                algorithm   = Algorithm.ROUNDROBIN;
                                select      = new RoundRobin();
                                break;
                            case "static-rr" :
                                algorithm   = Algorithm.STATICRR;
                                select      = new WeightedRoundRobin();
                                break;
                            case "iphash" :
                                algorithm   = Algorithm.IPHASHING;
                                select      = new IpHashing();
                                break;
                            case "leastconn" : 
                                //algorithm   = Algorithm.LEASTCONN;
                                //select      = new LeastConn();
                            default:
                                throw new Exception("Not supported LB method");
                        }

                        serverDistributor.setServerSelector(select);
              
                    }else if(splits[0].equals("server")){
                        serverDistributor.add(splits[1], splits[2], splits[3], 
                                                    splits.length == 4 ? DEFAULT_WEIGHT : splits[4]);
                    }
                }

                line = br.readLine();
            }

        }catch(FileNotFoundException e){
            log.error(e.toString());
        }catch(IOException i){
            log.error(i.toString()); 
        }catch(Exception e){
            log.error(e.toString());
        }finally{
            
            try{   
                fr.close();
                br.close();
            }catch(Exception e){
                System.err.println(e);
            }
        }
        
 
        if(isDefault)
            serverDistributor.setServerSelector(new RoundRobin());
       

    }
    
    /**
    * Waits until new client connection request comes.
    * 
    * When new connection request comes, creates new thread for this.
    * TODO: use a thread pool.
    */
    public void run(){
        log.info("Load Balancing Method   : {}", this.algorithm);
        log.info("running on PORT         : [{}]", this.port);
        serverDistributor.showList();
                 
                
        try{
            listenSock = new ServerSocket(this.port);
        }catch(IOException e){
            log.error(e.toString());
        }


        while(true){
                
            try{
                Socket cliSock = listenSock.accept();
                log.info("Client connection arrive : {}", cliSock.getInetAddress().toString());        
                
                
                // Select a backend server
                Info server = null;

                if(algorithm == Algorithm.IPHASHING)
                    server = serverDistributor.getSelectedServer(cliSock.getInetAddress());
                else
                    server = serverDistributor.getSelectedServer();


                log.info("[{}] ====> [{}]", cliSock.getInetAddress().toString(), server.getIp()+":"+server.getPort());
                
                // Make a worker
                Connection newCon = new Connection(cliSock, server);
                 
                // Handle the request
                Thread worker = new Thread(newCon);
                worker.start();
            
            }catch(IOException e){
                System.out.println(e);
            }
        }

    }

}

enum Algorithm{
    ROUNDROBIN("roundrobin"),
    STATICRR("static-rr"),
    IPHASHING("ip-hashing"),
    LEASTCONN("leastconn");

    private final String name;

    Algorithm(String name){ this.name = name;}

    public String getValue(){
        return name;
    }

}
