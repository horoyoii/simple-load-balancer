package org.horoyoii.greeter;

import java.io.*;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;


import org.horoyoii.manager.PeerManager;
import org.horoyoii.router.Router;
import org.horoyoii.worker.Worker;
import org.horoyoii.utils.Config;


import lombok.extern.slf4j.Slf4j;



/**

    Default 'server weight' is 1.
*/
@Slf4j
public class Greeter {

    private ExecutorService     executorService     = Executors.newFixedThreadPool(50);

    private ServerSocket        listenSock;     
    private int                 port;
           
    private final String        CONF_PATH           = "/home/horoyoii/Desktop/simple-load-balancer/load.conf";
  
    PeerManager                 peerManager         = new PeerManager();
    Router                      router              = new Router();

    public void init(){
        init0();
        initServerSocket();
    }


    /**
     * Waits until new client connection request comes.
     *  When new connection request comes, creates new thread for this.
     */
    public void run(){
        log.info("running on PORT         : [{}]", this.port);
        peerManager.showList();


        while(true){
                
            try{
                Socket cliSock = listenSock.accept();
                executorService.execute(new Worker(peerManager, router, cliSock));

            }catch(IOException e){
                log.error(e.toString());
            }
        }
    }


    /**
     *   Create a server socket.
     *
     *   @throws IOException
     */
    private void initServerSocket(){

        try{
            this.port = Config.getPort();
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
    private void init0(){
        log.info("read configuration file and init a peer manager");
        Config.read(CONF_PATH, peerManager, router);
        log.debug(router.toString());
    }


}

