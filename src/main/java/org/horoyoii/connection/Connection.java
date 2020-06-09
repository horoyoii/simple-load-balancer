package org.horoyoii.connection;

import java.net.*;
import java.io.*;

import org.horoyoii.http.header.HeaderDirective;
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

    private boolean             isKeepAlive;


    public Connection(PeerManager pm, Socket clientSocket){
        this.peerManager        = pm;
        this.clientSocket       = clientSocket;
    }

    @Override
    public void run(){

        /*
         * 1) Make a client socket input & output stream.
         */
        makeClientStream();


        /*
         * 2) Client ----------------> Proxy
         */
        HttpRequestMessage httpRequestMessage = new HttpRequestMessage(clientIn);


        /*
         * 3) Determine
         */
        String Url = httpRequestMessage.getURL();
        boolean isStaticContents = false;

        if(isStaticContents){
            log.info("static caching");
            responseService = new DirectoryResponseService();

        }else{
            log.info("goto upstream server");

            Peer peer = peerManager.getPeer(clientSocket.getInetAddress());
            responseService = new UpstreamResponseService(peerManager, peer);
        }


        /*
         * Client <------------------ Proxy
         */
        httpRequestMessage.addHeader(HeaderDirective.CONNECTION, HeaderDirective.CLOSE);
        HttpResponseMessage httpResponseMessage = responseService.getHttpResponseMessage(httpRequestMessage);
        writeHttpResponse(httpResponseMessage);

        responseService.close();
    }


    /**
     * build a input & output stream from client socket.
     */
    private void makeClientStream(){

        try{
            clientIn    = clientSocket.getInputStream();
            clientOut   = clientSocket.getOutputStream();
        }catch(IOException e){
            //TODO:
            e.printStackTrace();
        }

    }

//    private void oneWay(){
//        HttpResponseMessage httpResponseMessage =
//    }

    private HttpRequestMessage readHttpRequest(){
        return new HttpRequestMessage(clientIn);
    }


    /**
     * Send the response to client.
     *
     * @param httpResponseMessage
     */
    private void writeHttpResponse(HttpResponseMessage httpResponseMessage){
        log.info("write : client <---------- Proxy");

        try{
            //TODO : charset is what?
            clientOut.write(httpResponseMessage.toString().getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }

    }





//    //@Override
//    public void run0(){
//
//        /*
//         *  1) Generate a HTTP Request Object from client's input stream.
//         */
//        HttpRequestMessage httpRequestMessage = readHttpRequest();
//        log.debug(httpRequestMessage.toString());
//
//
//        /*
//         * 2) Determine
//         */
//        boolean isStaticContents = false;
//
//
//        /*
//         * 3) Generate a corresponding service object.
//         */
//        if(isStaticContents){
//            log.info("static caching");
//            responseService = new DirectoryResponseService();
//
//        }else{
//            log.info("goto upstream server");
//
//            Peer peer = peerManager.getPeer(clientSocket.getInetAddress());
//            responseService = new UpstreamResponseService(peer);
//        }
//
//        /*
//         * 4) Run it.
//         */
//        responseService.run(httpRequestMessage);
//
//
//        /*
//         * 5) Write it for waiting client.
//         */
//        HttpResponseMessage httpResponseMessage = responseService.getHttpResponseMessage();
//        log.debug(httpResponseMessage.toString());
//
//        writeHttpResponse(httpResponseMessage);
//   }



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

