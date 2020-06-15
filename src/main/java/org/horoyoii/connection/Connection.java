package org.horoyoii.connection;

import java.net.*;
import java.io.*;

import org.horoyoii.exception.ReadTimeoutException;
import org.horoyoii.http.constants.HttpDirective;
import org.horoyoii.manager.PeerManager;
import org.horoyoii.http.*;

import lombok.extern.slf4j.Slf4j;
import org.horoyoii.service.DirectoryResponseService;
import org.horoyoii.service.ResponseService;
import org.horoyoii.service.UpstreamResponseService;
import org.horoyoii.utils.HttpErrorRespHandler;


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

    private boolean             isKeepAlive = false;


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
        HttpRequestMessage httpRequestMessage;
        try{
            httpRequestMessage = new HttpRequestMessage(clientIn);
        }catch(ReadTimeoutException e){
            log.error(e.toString());
            //TODO : 408 Request Timeout exception.
            return;
        }


        if(!isKeepAlive)
            httpRequestMessage.addHeader(HttpDirective.CONNECTION, HttpDirective.CLOSE);


        /*
         * 3) Determine
         */
        String Url = httpRequestMessage.getURL();
        boolean isStaticContents = false;

        if(isStaticContents){
            responseService = new DirectoryResponseService();

        }else{

            /*
             * If all of the upstream servers are down, then servers 502 bad gateway response
             */
            responseService = new UpstreamResponseService(peerManager, clientSocket);
        }




        /*
         * Client <------------------ Proxy
         */
        HttpResponseMessage httpResponseMessage = responseService.getHttpResponseMessage(httpRequestMessage);



        if(!isKeepAlive)
            httpResponseMessage.addHeader(HttpDirective.CONNECTION, HttpDirective.CLOSE);

        writeHttpResponse(httpResponseMessage);

        try{
            clientSocket.close();
        }catch (IOException e){
            log.error(e.toString());
        }
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


    private HttpRequestMessage readHttpRequest() throws ReadTimeoutException{
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
            clientOut.write(httpResponseMessage.body.array());
        }catch (IOException e){
            e.printStackTrace();
        }

    }

}

