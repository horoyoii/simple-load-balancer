package org.horoyoii.loadbalance;

import java.io.*;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.HashMap;


import org.horoyoii.distribution.ServerDistributor;
import org.horoyoii.serverSelect.*;
import org.horoyoii.connection.Connection;
import org.horoyoii.model.Peer;
import org.horoyoii.util.ClassNameMapper;

import lombok.extern.slf4j.Slf4j;

/**


    Default 'server weight' is 1.
*/
@Slf4j
public class LoadBalancer{

    ServerDistributor   serverDistributor   = new ServerDistributor();
    ExecutorService     executorService     = Executors.newFixedThreadPool(50);

    ServerSocket        listenSock;     
    int                 port                = 9901;
           
    final String        DEFAULT_WEIGHT      = "1";
    final String        CONF_PATH           = "/home/horoyoii/Desktop/simple-load-balancer/load.config";
  

    public ServerSelector getServerSelector(String classAlias) throws Exception {
        return (ServerSelector)Class.forName(ClassNameMapper.getClassName(classAlias)).getDeclaredConstructor().newInstance();
    }

    
    /*
        Read the configuration file to set the load balancer setting.

        If there is no information about load balancing algorithm,
        then the default is RR.
        
        If there is no directive for the weight,
        the the default is 1.
    */

    public void readConfig(){
        log.info("read configuration...");
      
        BufferedReader br = null;
        
        try{
            br = new BufferedReader(new FileReader(new File(CONF_PATH)));
            String line = br.readLine();

            while(line != null){    
                if(line.length() != 0 && line.charAt(0) != '#'){ // ignore the comments(#)                     
                    String[] splits = line.split(" ");
                    if(splits[0].equals("balance")){

                        ServerSelector select = getServerSelector(splits[1]);                        
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
                br.close();
            }catch(Exception e){
                System.err.println(e);
            }
        }         
   }
    


    /**
    * Waits until new client connection request comes.
    * 
    * When new connection request comes, creates new thread for this.
    * TODO: use a thread pool.
    */
    public void run(){
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
                Peer server = serverDistributor.getSelectedServer(cliSock.getInetAddress());
                
                log.info("[{}] ====> [{}]", cliSock.getInetAddress().toString(), server.getIp()+":"+server.getPort());
                
                // handle the request
                executorService.execute(new Connection(executorService, cliSock, server));
                 
            }catch(IOException e){
                System.out.println(e);
            }
        }
    }
}

