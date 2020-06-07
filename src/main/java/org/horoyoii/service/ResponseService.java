package org.horoyoii.service;

import org.horoyoii.http.HttpRequestMessage;
import org.horoyoii.http.HttpResponseMessage;

/**
 * get a http response.
 *
 */
public abstract class ResponseService {

    HttpResponseMessage httpResponseMessage;

    public abstract void run(HttpRequestMessage httpRequestMessage);

    public HttpResponseMessage getHttpResponseMessage(){
        return httpResponseMessage;
    }



}
