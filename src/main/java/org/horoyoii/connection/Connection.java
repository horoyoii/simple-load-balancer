package org.horoyoii.connection;

import java.net.*;
import java.io.*;
import org.horoyoii.model.Info;


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
        
        try{
            servSock = new Socket(serverInfo.getIp(), serverInfo.getPort());
        }catch(Exception e){
            //TODO : throw it to the main.    
            System.out.println(e);
        }

        
        System.out.println("This worker requests to : " + serverInfo.getPort());

            try{

                OutputStream serverOs = servSock.getOutputStream();

                System.out.println(cliSock.getInetAddress());    
    
                InputStream is = cliSock.getInputStream();
            
                int readByte = 0;
                
                /*                 
                byte[] buf = new byte[5];
                readByte = is.read(buf, 0, 5);
                
                serverOs.write(buf);
                
                System.out.println("read bytes : "+readByte);
                String str = new String(buf);
                System.out.println("recv : "+str);
                */
                         
                while( (readByte = is.read()) != -1){
                    serverOs.write(readByte);
                    System.out.println((byte)readByte);
                }

                System.out.println(readByte);
            }catch(Exception e){
                System.out.println(e);    
            }


        System.out.println("Thread done");
    }
}
