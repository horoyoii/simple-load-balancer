package org.horoyoii.service;

import lombok.extern.slf4j.Slf4j;
import org.horoyoii.exception.NoLiveUpstreamException;
import org.horoyoii.exception.ReadTimeoutException;
import org.horoyoii.http.HttpRequestMessage;
import org.horoyoii.http.HttpResponseMessage;
import org.horoyoii.manager.PeerManager;
import org.horoyoii.model.Peer;
import org.horoyoii.utils.HttpErrorRespHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


/**
 *  Create a Http response.
 *
 *
 */
@Slf4j
public class UpstreamResponseService implements ResponseService {

    private Socket serverSock;

    private InputStream serverIn;
    private OutputStream serverOut;
    private Socket clientSocket;


    private PeerManager peerManager;
    private Peer peer;

    private int ReadTimeout = 5000;


    public UpstreamResponseService(PeerManager peerManager, Socket clientSocket) {
        this.peerManager    = peerManager;
        this.clientSocket   = clientSocket;
    }



    /**
     * Get a result from upstream server.
     *
      * @param httpRequestMessage
     */
    @Override
    public HttpResponseMessage getHttpResponseMessage(HttpRequestMessage httpRequestMessage) {

        /*
         *  Choose the upstream server to serve this request.
         */
        try{
            peer = peerManager.getPeer(clientSocket.getInetAddress());
        }catch(NoLiveUpstreamException n){
            return HttpErrorRespHandler.getErrorResponse(n.getHttpStatus());
        }


        try {
            log.debug("create server socket");
            serverSock = new Socket(peer.getIp(), peer.getPort());

            /*
             * Set ReadTimeout.
             */
            serverSock.setSoTimeout(ReadTimeout);


            serverIn = serverSock.getInputStream();
            serverOut = serverSock.getOutputStream();

        } catch (Exception e) {
            // TODO : throw it to the main.
            log.error(e.toString());
        }


        /*
         *                 Proxy ----------------->  Server
         */
        sendRequestToUpstream(httpRequestMessage);


        /*
         *                  Proxy <----------------- Server
         */
        HttpResponseMessage httpResponseMessage;

        try{
            httpResponseMessage = new HttpResponseMessage(serverIn);
        }catch (ReadTimeoutException r){
            return HttpErrorRespHandler.getErrorResponse(r.getHttpStatus());
        }finally{
            this.close();
        }

        return httpResponseMessage;
    }


    /**
     * Send HttpRequestMessage to upstream peer with socket io.
     *
     * @param httpRequestMessage
     */
    private void sendRequestToUpstream(HttpRequestMessage httpRequestMessage){
        try{
            //TODO : charset is what?
            serverOut.write(httpRequestMessage.toString().getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    private void close(){
        try{
            serverOut.close();
            if(!serverSock.isClosed())
                serverSock.close();
        }catch (IOException e){
            log.info(e.toString());
        }finally{
            peerManager.releasePeer(peer);
        }
    }


}
