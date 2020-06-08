package org.horoyoii.loadbalance;

import java.io.*;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;


import org.horoyoii.manager.PeerManager;
import org.horoyoii.connection.Connection;
import org.horoyoii.model.Peer;
import org.horoyoii.utils.Configuration;


import lombok.extern.slf4j.Slf4j;



/**

    Default 'server weight' is 1.
*/
@Slf4j
public class LoadBalancer{

    private ExecutorService     executorService     = Executors.newFixedThreadPool(50);

    private ServerSocket        listenSock;     
    private int                 port                = 9904;
           
    private final String        CONF_PATH           = "/home/horoyoii/Desktop/simple-load-balancer/load.conf";
  
    PeerManager                 peerManager;
 

    public void init(){
        log.info("Initialization...");
        initPeerManager();
        initServerSocket();
    }


    /**
     * Waits until new client connection request comes.
     * 
     * When new connection request comes, creates new thread for this.
     */
    public void run(){
        log.info("running on PORT         : [{}]", this.port);
        peerManager.showList();
                 
        while(true){
                
            try{
                    Socket cliSock = listenSock.accept();
                    log.info("Client connection arrive : {}", cliSock.getInetAddress().toString());                             

                    Peer peer = peerManager.getPeer(cliSock.getInetAddress());
                
                    log.info("[{}] ====> [{}]", cliSock.getInetAddress().toString(), 
                                                    peer.getIp()+":"+peer.getPort());               

                    executorService.execute(new Connection(peerManager, cliSock));
                 
            }catch(IOException e){
                System.out.println(e);
            }
        }
    }



    /**
     *   Create a server socket.
     *
     *   @throws IOException
     */
    private void initServerSocket(){
        log.info("server socket creation...");

        try{
            listenSock = new ServerSocket(this.port);

        }catch(IOException e){
            log.error(e.toString());
            System.exit(0);
        }
    }

    /**
     *  Create a Peer Manager.
     * 
     */        
    private void initPeerManager(){
        log.info("read configuration file and init a peer manager");
        peerManager = new PeerManager();
        Configuration.read(CONF_PATH, peerManager);        
    }

}

