package org.horoyoii.connection;

import java.net.*;
import java.io.*;
import org.horoyoii.model.Info;

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

    private Info                serverInfo;
    private Socket              servSock;
    
    private boolean             isActive    = true;
       
    public Connection(Socket cliSock, Info info){
        this.cliSock = cliSock;
        this.serverInfo = info;   
    }
      
    @Override
    public void run(){
        
        InputStream     clientIn;
        OutputStream    clientOut;
        InputStream     serverIn;
        OutputStream    serverOut;
        
                
        try{
            servSock = new Socket(serverInfo.getIp(), serverInfo.getPort());

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
        
        Thread clientToServer = new Thread(new IoBridge(this, clientIn, serverOut)); 
        Thread serverToClient = new Thread(new IoBridge(this, serverIn, clientOut));
        
        
        log.info("I/O Bridge is starting");       
        clientToServer.start();
        serverToClient.start();
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
