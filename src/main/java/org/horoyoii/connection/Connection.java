package org.horoyoii.connection;

import java.net.*;
import java.io.*;
import java.util.concurrent.ExecutorService;

import org.horoyoii.model.Peer;

import lombok.extern.slf4j.Slf4j;


/**

    Connection makes two streams.

    One is for upstream which is the direction from the client to the proxy to the server.
    The other is for downstream which is the direction from the server to the porxy to the client.

    These two streams are running on two threads.
*/
@Slf4j
public class Connection implements Runnable {
    
    private Socket              cliSock;

    private Peer                serverPeer;
    private Socket              servSock;
    
    private boolean             isActive    = true;

    private ExecutorService     executorService;
       
    public Connection(ExecutorService executorService, Socket cliSock, Peer info){
        this.executorService    = executorService;
        this.cliSock            = cliSock;
        this.serverPeer         = info;   
    }
      
    @Override
    public void run(){
        
        InputStream     clientIn;
        OutputStream    clientOut;
        InputStream     serverIn;
        OutputStream    serverOut;
        
                
        try{
            servSock = new Socket(serverPeer.getIp(), serverPeer.getPort());

            clientIn    = cliSock.getInputStream();
            clientOut   = cliSock.getOutputStream();
            serverIn    = servSock.getInputStream();
            serverOut   = servSock.getOutputStream();    
             
        }catch(Exception e){
            //TODO : throw it to the main.    
            System.out.println(e);
            return;
        }
        
        
        // Forwading data from client to server and vice versa. 
        executorService.execute(new IoBridge(this, clientIn, serverOut));
        executorService.execute(new IoBridge(this, serverIn, clientOut));
        
        log.info("I/O Bridge is starting");     
   }

    
    public synchronized void closeConnection(){
        
        if(isActive){
            isActive = false;
            
            log.info("close connection");
    
            try{
                cliSock.close();
            }catch(IOException e){
                log.error(e.toString());
            }
    
            try{
                servSock.close();
            }catch(IOException e){
                log.error(e.toString());
            }                 
        }

    }
}

