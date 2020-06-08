package org.horoyoii.connection;

import java.net.*;
import java.io.*;
import org.horoyoii.manager.PeerManager;
import org.horoyoii.model.Peer;
import org.horoyoii.http.*;

import lombok.extern.slf4j.Slf4j;
import org.horoyoii.service.DirectoryResponseService;
import org.horoyoii.service.ResponseService;
import org.horoyoii.service.UpstreamResponseService;


/**
    Connection

    1) Generate a HTTP Request Object from client's input stream.
    2) Determine from where a response should come.
        2-1) from directory.
        2-2) from upstream server.
    3) Generate a corresponding service object.
    4) Run it.

*/
@Slf4j
public class Connection implements Runnable {
    
    private Socket              clientSocket;

    private InputStream         clientIn;
    private OutputStream        clientOut;

    private ResponseService     responseService;

    private PeerManager         peerManager;


    public Connection(PeerManager pm, Socket clientSocket){
        this.peerManager        = pm;
        this.clientSocket       = clientSocket;
    }


    /**
     * build a input & output stream from client socket.
     */
    private void buildClientStream(){
        try{
            clientIn    = clientSocket.getInputStream();
            clientOut   = clientSocket.getOutputStream();
        }catch(IOException e){
            //TODO:
            e.printStackTrace();
        }
    }



    private HttpRequestMessage readHttpRequest(){
        return new HttpRequestMessage(clientIn);
    }


    /**
     * Send the response to client.
     *
     * @param httpResponseMessage
     */
    private void writeHttpResponse(HttpResponseMessage httpResponseMessage){

        try{
            //TODO : charset is what?
            clientOut.write(httpResponseMessage.toString().getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }

    }


    @Override
    public void run(){
        buildClientStream();

        /*
         *  1) Generate a HTTP Request Object from client's input stream.
         */

        HttpRequestMessage httpRequestMessage = readHttpRequest();
        System.out.println(httpRequestMessage);


        /*
         * 2) Determine
         */

        boolean isStaticContents = false;


        /*
         * 3) Generate a corresponding service object.
         */

        if(isStaticContents){
            log.info("static caching");
            responseService = new DirectoryResponseService();

        }else{
            log.info("goto upstream server");

            Peer peer = peerManager.getPeer(clientSocket.getInetAddress());
            responseService = new UpstreamResponseService(peer);
        }

        /*
         * 4) Run it.
         */
        responseService.run(httpRequestMessage);


        /*
         * 5) Write it for waiting client.
         */
        HttpResponseMessage httpResponseMessage = responseService.getHttpResponseMessage();
        log.debug(httpResponseMessage.toString());

        writeHttpResponse(httpResponseMessage);


        // First Step
        // Client  -----------> Proxy


        //((HttpRequestMessage)hm).getURL();

        /* pseudo code

            url = hm.getUrl()
            if url is registered in static cache list
            then
                go to static caching module
            else
                go to upstream server

            return response

         */

        //                      Proxy ------------->  Upstream Server



        //                      Proxy <------------- Upstream Server


        // Client <------------ Proxy


        // Forwading data from client to server and vice versa. 
        //executorService.execute(new IoBridge(this, clientIn, serverOut));
        //executorService.execute(new IoBridge(this, serverIn, clientOut));
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
    
//    public synchronized void closeConnection(){
//
//        if(isActive){
//            isActive = false;
//
//            log.info("close connection");
//
//            try{
//                clientSocket.close();
//            }catch(IOException e){
//                log.error(e.toString());
//            }
//
//            try{
//                servSock.close();
//            }catch(IOException e){
//                log.error(e.toString());
//            }
//
//            peerManager.decreaseCount(peer);
//        }
//    }
}

