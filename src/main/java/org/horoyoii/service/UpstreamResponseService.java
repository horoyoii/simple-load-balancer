package org.horoyoii.service;

import lombok.extern.slf4j.Slf4j;
import org.horoyoii.http.HttpRequestMessage;
import org.horoyoii.http.HttpResponseMessage;
import org.horoyoii.manager.PeerManager;
import org.horoyoii.model.Peer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;



/**
 *  Create a Http response based on upstream server.
 */
@Slf4j
public class UpstreamResponseService implements ResponseService {

    private Socket serverSock;
    private InputStream serverIn;
    private OutputStream serverOut;

    private PeerManager peerManager;
    private Peer peer;

    public UpstreamResponseService(PeerManager peerManager, Peer peer) {
        this.peerManager = peerManager;
        this.peer        = peer;


        try {
            serverSock = new Socket(peer.getIp(), peer.getPort());
            serverIn = serverSock.getInputStream();
            serverOut = serverSock.getOutputStream();

        } catch (Exception e) {
          // TODO : throw it to the main.
          System.out.println(e);
        }
    }



    /**
     * Get a result from upstream server.
     *
      * @param httpRequestMessage
     */
    @Override
    public HttpResponseMessage getHttpResponseMessage(HttpRequestMessage httpRequestMessage) {
        //log.debug(httpRequestMessage.toString());

        /*
         *                 Proxy ----------------->  Server
         */
        sendRequestToUpstream(httpRequestMessage);


        /*
         *                  Proxy <----------------- Server
         */
        HttpResponseMessage httpResponseMessage = new HttpResponseMessage(serverIn);
        //log.debug(httpResponseMessage.toString());


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


    @Override
    public void close(){
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
