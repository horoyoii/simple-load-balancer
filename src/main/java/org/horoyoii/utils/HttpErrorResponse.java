package org.horoyoii.utils;

import org.horoyoii.http.HttpResponseMessage;
import org.horoyoii.http.header.Header;
import org.horoyoii.http.header.HeaderDirective;
import org.horoyoii.http.startLine.HttpStatus;
import org.horoyoii.http.startLine.ResponseStatusLine;

import java.nio.ByteBuffer;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class HttpErrorResponse {

    public static HttpResponseMessage getErrorResponse(HttpStatus httpStatus){
        HttpResponseMessage httpResponseMessage = new HttpResponseMessage();
        httpResponseMessage.setStartLine(new ResponseStatusLine("HTTP/1.1", String.valueOf(httpStatus.getStatusCode()), httpStatus.getStatusText()));

        Header header = new Header();
        header.setHeader(HeaderDirective.DATE, getCurrentTime());

        if(httpStatus.equals(HttpStatus.BAD_GATEWAY)){

            header.setHeader(HeaderDirective.CONTENT_LENGTH, EM_502.length());
            header.setHeader(HeaderDirective.CONTENT_TYPE, "text/html");
            httpResponseMessage.setHeader(header);

            httpResponseMessage.setBody(ByteBuffer.wrap(EM_502.getBytes()));
        }

        return httpResponseMessage;
    }


    private static String getCurrentTime(){
        ZonedDateTime znt = ZonedDateTime.now(ZoneId.of("UTC"));
        return znt.format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }

    final static String EM_502 = "<html>\n" +
            "<head><title>502 Bad Gateway</title></head>\n" +
            "<body bgcolor=\"white\">\n" +
            "<center><h1>502 Bad Gateway</h1></center>\n" +
            "<hr><center>Horoyoii/1.14.0 (Ubuntu)</center>\n" +
            "</body>\n" +
            "</html>";

}
