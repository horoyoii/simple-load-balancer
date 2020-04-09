package org.horoyoii.loadbalance;

import java.io.*;
import java.net.*;

import org.horoyoii.distribution.ServerDistributor;
import org.horoyoii.serverSelect.*;
import org.horoyoii.connection.Connection;
import org.horoyoii.model.Info;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoadBalancer{

    ServerDistributor   serverDistributor   = new ServerDistributor();
    ServerSocket        listenSock;     
    int                 port                = 9901;       
    
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
        
        boolean staticrr = false, isDefault = true;

        try{
            f = new File(path);
            fr = new FileReader(f);
            br = new BufferedReader(fr);
 
            String line = br.readLine();
            while(line != null){
                
                if(line.length() != 0 && line.charAt(0) != '#'){                      
                    String[] splits = line.split(" ");

                    if(splits[0].equals("balance")){
                        isDefault = false;
                        ServerSelector select = null;

                        switch( splits[1] ){
                            case "roundrobin" :
                                select = new RoundRobin();
                                break;
                            case "static-rr" :
                                select = new WeightedRoundRobin();
                                staticrr = true;
                                break;
                        }

                        serverDistributor.setServerSelector(select);
              
                    }else if(splits[0].equals("server")){

                        if(staticrr)
                            serverDistributor.add(splits[1], splits[2], splits[3], splits[4]);
                        else
                            serverDistributor.add(splits[1], splits[2], splits[3]);
                    }
                }

                line = br.readLine();
            }

        }catch(FileNotFoundException e){
            System.err.println(e);
        }catch(IOException i){
            System.err.println(i);   
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
        

        serverDistributor.showList();
    }

    public void run(){
        log.info("Start running on PORT [{}]", this.port);
         
        try{
            listenSock = new ServerSocket(9901);
        }catch(IOException e){
            System.out.println(e);
        }


        while(true){
                
            try{
                Socket cliSock = listenSock.accept();
                log.info("Client connection arrive : {}", cliSock.getInetAddress().toString());        

                // Select a backend server
                Info server = serverDistributor.getSelectedServer();
                
                log.info("Request from [{}] is FORWARDED to backend [{}]", cliSock.getInetAddress().toString(), server.getIp()+":"+server.getPort());
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
