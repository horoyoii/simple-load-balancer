package org.horoyoii.loadbalance;

import java.io.*;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.HashMap;


import org.horoyoii.manager.PeerManager;
import org.horoyoii.serverSelect.*;
import org.horoyoii.connection.Connection;
import org.horoyoii.model.Peer;
import org.horoyoii.util.ClassNameMapper;
import org.horoyoii.exception.AlgoNotValidException;


import lombok.extern.slf4j.Slf4j;



/**


    Default 'server weight' is 1.
*/
@Slf4j
public class LoadBalancer{

    PeerManager   peerManager   = new PeerManager();
    ExecutorService     executorService     = Executors.newFixedThreadPool(50);

    ServerSocket        listenSock;     
    int                 port                = 9901;
           
    final String        DEFAULT_WEIGHT      = "1";
    final String        CONF_PATH           = "/home/horoyoii/Desktop/simple-load-balancer/load.conf";
  
     

    /*
        Read the configuration file to set the load balancer setting.

        If there is no information about load balancing algorithm,
        then the default is RR.
        
        If there is no directive for the weight,
        the the default is 1.
    */
    public void readConfig(){
        log.info("read configuration...");
      
        try(
            BufferedReader br = new BufferedReader(new FileReader(new File(CONF_PATH)));
        ){
            String line = br.readLine();

            while(line != null)
            {    
                if(line.length() != 0 && line.charAt(0) != '#'){ // ignore the comments(#)                     
                    String[] splits = line.split(" ");
                    
                    if(splits[0].equals("algo"))
                    {           
                        try{         
                            peerManager.setServerSelector( getServerSelector(splits[1]) );
                        }catch(AlgoNotValidException a){
                            //TODO: use global exception handler?? 
                            // setDefaultUncaughtExceptionHandler();

                            a.printStackTrace();
                            System.exit(0);     
                        }catch(Exception e){
                            System.exit(0);
                        }

                    }else if(splits[0].equals("server")){
                        peerManager.add(splits[1], splits[2], splits[3], 
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
        peerManager.showList();
                 
        try{
            listenSock = new ServerSocket(this.port);
        }catch(IOException e){
            log.error(e.toString());
        }

        while(true){
                
            try{
                Socket cliSock = listenSock.accept();
                log.info("Client connection arrive : {}", cliSock.getInetAddress().toString());                             
                /** 
                    Get a backend server to handle this connection
                */
                Peer peer = peerManager.getPeer(cliSock.getInetAddress());
                
                log.info("[{}] ====> [{}]", cliSock.getInetAddress().toString(), 
                                                    peer.getIp()+":"+peer.getPort());
                
                // handle the request
                //TODO : Builder Pattern for Connection Class
                executorService.execute(new Connection(executorService, peerManager, cliSock, peer));
                 
            }catch(IOException e){
                System.out.println(e);
            }
        }
    }

    
    public ServerSelector getServerSelector(String classAlias) throws Exception{
        ServerSelector ss = null;

        try{
            ss =(ServerSelector)Class.forName(ClassNameMapper.getClassName(classAlias))
                                                    .getDeclaredConstructor().newInstance();
        }catch(ClassNotFoundException e){
            throw new AlgoNotValidException(classAlias+" is not valid algorithm name");
        }

        return ss;
    }


}

