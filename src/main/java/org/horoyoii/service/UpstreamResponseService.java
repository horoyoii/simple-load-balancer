package org.horoyoii.service;

import lombok.extern.slf4j.Slf4j;
import org.horoyoii.http.HttpRequestMessage;
import org.horoyoii.http.HttpResponseMessage;
import org.horoyoii.model.Peer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;



/**
 *  Create a Http response based on upstream server.
 */
@Slf4j
public class UpstreamResponseService extends ResponseService {

    private Socket serverSock;
    private InputStream serverIn;
    private OutputStream serverOut;

    public UpstreamResponseService(Peer peer) {

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
    public void run(HttpRequestMessage httpRequestMessage) {

        /*
         *                 Proxy ----------------->  Server
         */

        log.info("write to server.");
        try{
            //TODO : charset is what?
            serverOut.write(httpRequestMessage.toString().getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }


        /*
         *                  Proxy <----------------- Server
         */
        log.info("foo===============");
        httpResponseMessage = new HttpResponseMessage(serverIn);
    }

}
