package org.horoyoii.service;

import lombok.extern.slf4j.Slf4j;
import org.horoyoii.exception.NoLiveUpstreamException;
import org.horoyoii.exception.ReadTimeoutException;
import org.horoyoii.http.HttpRequestMessage;
import org.horoyoii.http.HttpResponseMessage;
import org.horoyoii.manager.PeerManager;
import org.horoyoii.model.Location;
import org.horoyoii.model.Peer;
import org.horoyoii.utils.HttpErrorRespHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


/**
 *
 *
 *
 */
@Slf4j
public class UpstreamResponseService implements ResponseService {

    private Socket upstreamSocket;
    private InputStream upstreamIn;
    private OutputStream upstreamOut;

    private Socket clientSocket;

    private HttpRequestMessage httpRequestMessage;
    private PeerManager peerManager;
    private Peer peer;
    private Location location;

    private static int ReadTimeout = 5000;


    public UpstreamResponseService(Location location, PeerManager peerManager, Socket clientSocket, HttpRequestMessage httpRequestMessage) {
        this.location           = location;
        this.peerManager        = peerManager;
        this.clientSocket       = clientSocket;
        this.httpRequestMessage = httpRequestMessage;
    }



    /**
     * Get a http response from selected upstream server.
     *
     */
    @Override
    public HttpResponseMessage getHttpResponseMessage() {
        log.debug(location.toString());


        // Choose the upstream server to serve this request.
        try{
            peer = peerManager.getPeer(clientSocket.getInetAddress());
        }catch(NoLiveUpstreamException n){
            return HttpErrorRespHandler.getErrorResponse(n.getHttpStatus());
        }


        createUpstreamSocket();


        // Proxy ----------------->  Server
        replaceRequestTarget(location.getPattern(), httpRequestMessage);
        sendRequestToUpstream(httpRequestMessage);


        // Proxy <----------------- Server
        HttpResponseMessage httpResponseMessage;


        try{
            httpResponseMessage = new HttpResponseMessage(upstreamIn);
        }catch (ReadTimeoutException r){
            return HttpErrorRespHandler.getErrorResponse(r.getHttpStatus());    // 504 Gateway Timeout
        }finally{
            this.close();
        }


        return httpResponseMessage;
    }


    /**
     * Send HttpRequestMessage to upstream peer with socket io.
     *
     * @param httpRequestMessage The message from a client.
     */
    private void sendRequestToUpstream(HttpRequestMessage httpRequestMessage){
        try{
            //TODO : charset is what?
            upstreamOut.write(httpRequestMessage.toString().getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    private void replaceRequestTarget(String pattern, HttpRequestMessage httpRequestMessage){
        String oldTarget = httpRequestMessage.getRequestTarget();
        String newTarget = oldTarget.substring(pattern.length()-1);
        httpRequestMessage.setRequestTarget(newTarget);
    }


    private void createUpstreamSocket(){
        log.debug("create a upstream socket");
        try {
            upstreamSocket = new Socket(peer.getIp(), peer.getPort());
            upstreamSocket.setSoTimeout(ReadTimeout);

            upstreamIn = upstreamSocket.getInputStream();
            upstreamOut = upstreamSocket.getOutputStream();

        } catch (Exception e) {
            // TODO : throw it to the main.
            log.error(e.toString());
        }
    }


    private void close(){
        try{
            upstreamOut.close();
            if(!upstreamSocket.isClosed())
                upstreamSocket.close();
        }catch (IOException e){
            log.info(e.toString());
        }finally{
            peerManager.releasePeer(peer);
        }
    }


}
