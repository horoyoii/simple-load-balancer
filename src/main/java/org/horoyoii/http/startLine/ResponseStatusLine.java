package org.horoyoii.http.startLine;


import lombok.Builder;

/**
 * 
 * Format : [Protocol Version] [Status Code] [Status Text]
 *
 * ex) HTTP/1.1 200 OK
 *
 */
public class ResponseStatusLine extends StartLine {

    private String statusCode;         //TODO : enum
    private String statusText;      // 
    
    public ResponseStatusLine(String protocol, String statusCode, String statusText){
        super(protocol);
        this.statusCode = statusCode;
        this.statusText = statusText;
    }


    public ResponseStatusLine(String[] tokens){
        this(tokens[0], tokens[1], tokens[2]);
    }


    @Override
    public String toString() {
        return protocol+" "+statusCode+" "+statusText;
    }
}
