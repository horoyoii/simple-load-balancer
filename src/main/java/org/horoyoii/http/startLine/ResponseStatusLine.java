package org.horoyoii.http.startLine;


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

    @Override
    public String toString() {
        return protocol+" "+statusCode+" "+statusText;
    }
}
