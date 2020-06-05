package org.horoyoii.http.startLine;


/**
 * 
 * Format : [Protocol Version] [Status Code] [Status Text]
 *
 * ex) HTTP/1.1 200 OK
 *
 */
public class ResponseStatusLine extends StartLine {

    private int statusCode;         //TODO : enum
    private String statusText;      // 
    

}
