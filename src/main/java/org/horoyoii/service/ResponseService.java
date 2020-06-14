package org.horoyoii.service;

import org.horoyoii.http.HttpRequestMessage;
import org.horoyoii.http.HttpResponseMessage;

/**
 * get a http response.
 *
 */
public interface ResponseService {

    HttpResponseMessage getHttpResponseMessage(HttpRequestMessage httpRequestMessage);

    //void close();
}
