package org.horoyoii.service;

import org.horoyoii.http.HttpRequestMessage;
import org.horoyoii.http.HttpResponseMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;

/**
 * Create a HTTP response based on a file system.
 *
 */
public class DirectoryResponseService implements ResponseService{

    /**
     * Get a result from file system.
     *
     * @param httpRequestMessage
     */
    @Override
    public HttpResponseMessage getHttpResponseMessage(HttpRequestMessage httpRequestMessage) {

        String Url = httpRequestMessage.getURL();

        makeFilePath();


        // httpRequestMessage = new HttpRequestMessage();
        return new HttpResponseMessage();
    }


    void makeFilePath(){

    }
}
