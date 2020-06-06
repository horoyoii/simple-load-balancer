package org.horoyoii.connection;

import java.net.*;
import java.io.*;
import java.util.concurrent.ExecutorService;

import org.horoyoii.manager.PeerManager;
import org.horoyoii.model.Peer;
import org.horoyoii.http.*;

import lombok.extern.slf4j.Slf4j;


/**

    Connection makes two streams.

    One is for upstream which is the direction from the client to the proxy to the server.
    The other is for downstream which is the direction from the server to the porxy to the client.

    These two streams are running on two threads.
*/
@Slf4j
public class Connection implements Runnable {
    
    private Socket              clientSocket;

    private Peer                peer;
    private Socket              servSock;
    
    private boolean             isActive    = true;

    private ExecutorService     executorService;
    private PeerManager         peerManager;

       
    public Connection(ExecutorService executorService, PeerManager pm, Socket clientSocket, Peer peer){
        this.executorService    = executorService;
        this.peerManager        = pm;
        this.clientSocket       = clientSocket;
        this.peer               = peer;   
    }

      
    @Override
    public void run(){
        
        InputStream     clientIn;
        OutputStream    clientOut;
        InputStream     serverIn;
        OutputStream    serverOut;
        
        try{
            servSock = new Socket(peer.getIp(), peer.getPort());

            clientIn    = clientSocket.getInputStream();
            clientOut   = clientSocket.getOutputStream();
            serverIn    = servSock.getInputStream();
            serverOut   = servSock.getOutputStream();    
             
        }catch(Exception e){
            //TODO : throw it to the main.    
            System.out.println(e);
            return;
        }
        
        log.info("----------------------------------");
        HttpMessage hm = new HttpRequestMessage(clientIn);
        System.out.println(hm.getBody());

        //readRequest(clientIn);
        
        log.info("----------------------------------");
                 
         
        
        // Forwading data from client to server and vice versa. 
        executorService.execute(new IoBridge(this, clientIn, serverOut));
        executorService.execute(new IoBridge(this, serverIn, clientOut));
        
        log.info("I/O Bridge is starting");     
   }


    @Deprecated
    void readRequest(InputStream inputStream){
		
        StringBuffer line = new StringBuffer();
		int byteOfData = -1;

		try {

			while ((byteOfData = inputStream.read()) != -1) {
			    char readChar = (char) byteOfData;
				System.out.print(readChar);

//				if (readChar == '\n') {
//					break;
//				} else {
//					if (readChar == '\r') {
//						continue;
//					}
//					line.append(readChar);
//
			//}
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
        }
		System.out.println("closed()");
		System.out.println(byteOfData);

    }
    
    public synchronized void closeConnection(){
        
        if(isActive){
            isActive = false;
                
            log.info("close connection");
        
            try{
                clientSocket.close();
            }catch(IOException e){
                log.error(e.toString());
            }
    
            try{
                servSock.close();
            }catch(IOException e){
                log.error(e.toString());
            }
            
            peerManager.decreaseCount(peer);                 
        }

    }
}

