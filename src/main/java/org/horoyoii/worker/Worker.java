package org.horoyoii.worker;

import java.net.*;
import java.io.*;

import org.horoyoii.exception.ReadTimeoutException;
import org.horoyoii.http.constants.HttpDirective;
import org.horoyoii.manager.PeerManager;
import org.horoyoii.http.*;

import lombok.extern.slf4j.Slf4j;
import org.horoyoii.model.Location;
import org.horoyoii.router.Router;
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
public class Worker implements Runnable {
    
    private Socket              clientSocket;

    private InputStream         clientIn;
    private OutputStream        clientOut;

    private PeerManager         peerManager;
    private Router              router;

    private boolean             isKeepAlive = false;


    public Worker(PeerManager pm, Router router, Socket clientSocket){
        this.peerManager        = pm;
        this.clientSocket       = clientSocket;
        this.router             = router;
    }


    @Override
    public void run(){
        HttpRequestMessage httpRequestMessage;

        // 1) Make a client socket input & output stream.
        buildClientStream();



        // 2) Client ----------------> Proxy
        try{
            httpRequestMessage = buildRequestMessage();
        }catch (ReadTimeoutException e){
            //TODO;
            return;
        }

        if(!isKeepAlive)
            httpRequestMessage.addHeader(HttpDirective.CONNECTION, HttpDirective.CLOSE);



        // 3) Determine
        String uri = httpRequestMessage.getURL();
        Location location = router.getLocation(uri);
        log.debug(location.toString());
        ResponseService responseService = getResponseService(location, httpRequestMessage);



        // 4) Client <------------------ Proxy
        HttpResponseMessage httpResponseMessage = responseService.getHttpResponseMessage();

        if (!isKeepAlive) {
            httpResponseMessage.addHeader(HttpDirective.CONNECTION, HttpDirective.CLOSE);
        }

        writeHttpResponse(httpResponseMessage);

        try{
            clientSocket.close();
        }catch (IOException e){
            log.error(e.toString());
        }
    }


    private ResponseService getResponseService(Location location, HttpRequestMessage httpRequestMessage){
        if(location.getFrom().equals(Location.FROM_FS)){
            return new DirectoryResponseService(location, httpRequestMessage.getURL());

        }else if(location.getFrom().equals(Location.FROM_UPSTREAM)){

            //If all of the upstream servers are down, then servers 502 bad gateway response
            return new UpstreamResponseService(location, peerManager, clientSocket, httpRequestMessage);
        }

        //TODO : Fix the condition where null will be returned.
        return null;
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


    private HttpRequestMessage buildRequestMessage() throws ReadTimeoutException{

        try{
            return new HttpRequestMessage(clientIn);
        }catch(ReadTimeoutException e){
            //TODO : 408 Request Timeout exception.
            log.error(e.toString());
            throw new ReadTimeoutException("todo");
        }
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

