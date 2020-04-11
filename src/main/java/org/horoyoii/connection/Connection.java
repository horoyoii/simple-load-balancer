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
        }
        
            
        log.info("This worker requests to : " + serverInfo.getPort());


        log.info("worker done");
    }
}
