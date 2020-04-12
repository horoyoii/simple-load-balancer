package org.horoyoii.connection;

import java.net.*;
import java.io.*;
import org.horoyoii.model.Info;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Connection implements Runnable {
    
    private Socket              cliSock;

    private Info                serverInfo;
    private Socket              servSock;
    
    private Thread              t;
     
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
        
        log.info("tid "+clientToServer.getId());
        log.info("tid "+serverToClient.getId());
        
        clientToServer.start();
        serverToClient.start();
        
        log.info("Forwading started");

        // and this thread for connetion is terminated
    }

    public void setThread(Thread t){
        this.t = t;
    }
    public void getState(){
        System.out.println("parent tid : " +t.getId());
        System.out.println("parent state : " + t.getState().toString());
    }
}
