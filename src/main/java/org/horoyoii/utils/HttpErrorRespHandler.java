package org.horoyoii.utils;

import org.horoyoii.http.HttpResponseMessage;
import org.horoyoii.http.header.Header;
import org.horoyoii.http.constants.HttpDirective;
import org.horoyoii.http.constants.HttpStatus;
import org.horoyoii.http.startLine.ResponseStatusLine;

import java.nio.ByteBuffer;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class HttpErrorRespHandler {


    public static HttpResponseMessage getErrorResponse(HttpStatus httpStatus){
        HttpResponseMessage httpResponseMessage = new HttpResponseMessage();
        httpResponseMessage.setStartLine(new ResponseStatusLine("HTTP/1.1", String.valueOf(httpStatus.getStatusCode()), httpStatus.getStatusText()));

        Header header = new Header();
        header.setHeader(HttpDirective.DATE, getCurrentTime());
        header.setHeader(HttpDirective.SERVER, getServerInfo());


        if(httpStatus.equals(HttpStatus.BAD_GATEWAY)){

            header.setHeader(HttpDirective.CONTENT_LENGTH, EM_502.length());
            header.setHeader(HttpDirective.CONTENT_TYPE, "text/html");
            httpResponseMessage.setHeader(header);

            httpResponseMessage.setBody(ByteBuffer.wrap(EM_502.getBytes()));
        }
        else if(httpStatus.equals(HttpStatus.GATEWAY_TIMEOUT)){

            header.setHeader(HttpDirective.CONTENT_LENGTH, EM_504.length());
            header.setHeader(HttpDirective.CONTENT_TYPE, "text/html");
            httpResponseMessage.setHeader(header);

            httpResponseMessage.setBody(ByteBuffer.wrap(EM_504.getBytes()));
        }

        return httpResponseMessage;
    }


    private static String getCurrentTime(){
        ZonedDateTime znt = ZonedDateTime.now(ZoneId.of("UTC"));
        return znt.format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }


    private static String getServerInfo(){
        String n = "HginX/0.1";
        String os = System.getProperty("os.name");
        return n + " ("+os+")";
    }


    final static String EM_502 = "<html>\n" +
            "<head><title>502 Bad Gateway</title></head>\n" +
            "<body bgcolor=\"white\">\n" +
            "<center><h1>502 Bad Gateway</h1></center>\n" +
            "<hr><center>HginX/0.1 (Ubuntu)</center>\n" +
            "</body>\n" +
            "</html>";


    final static String EM_504 = "<html>\n" +
            "<head><title>504 Gateway Time-out</title></head>\n" +
            "<body bgcolor=\"white\">\n" +
            "<center><h1>504 Gateway Time-out</h1></center>\n" +
            "<hr><center>Hginx/0.1 (Ubuntu)</center>\n" +
            "</body>\n" +
            "</html>\n";

}
