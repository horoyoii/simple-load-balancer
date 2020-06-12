package org.horoyoii.service;

import lombok.extern.slf4j.Slf4j;
import org.horoyoii.exception.NoLiveUpstreamException;
import org.horoyoii.http.HttpRequestMessage;
import org.horoyoii.http.HttpResponseMessage;
import org.horoyoii.http.header.HeaderDirective;
import org.horoyoii.manager.PeerManager;
import org.horoyoii.model.Peer;
import org.horoyoii.utils.HttpErrorResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.http.HttpHeaders;


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

    public UpstreamResponseService(PeerManager peerManager, Socket clientSocket) {
        this.peerManager    = peerManager;
        this.peer           = peer;
        this.clientSocket   = clientSocket;


        try {
            //TODO:
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

        /*
         *  Choose the upstream server to serve this request.
         */
        try{
            Peer peer = peerManager.getPeer(clientSocket.getInetAddress());
        }catch(NoLiveUpstreamException n){
            //TODO : handle exception

            return HttpErrorResponse.getErrorResponse(n.getHttpStatus());
        }


        /*
         *                 Proxy ----------------->  Server
         */
        sendRequestToUpstream(httpRequestMessage);


        /*
         *                  Proxy <----------------- Server
         */
        HttpResponseMessage httpResponseMessage = new HttpResponseMessage(serverIn);

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
