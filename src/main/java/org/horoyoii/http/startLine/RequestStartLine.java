package org.horoyoii.http.startLine;


import lombok.Getter;

/**
 * [method] [url] [protocol/version]
 *
 * ex) POST /user/hello HTTP/1.0
 *
 */
@Getter
public class RequestStartLine extends StartLine {

    private String method;
    private String url;


    public RequestStartLine(String method, String url, String protocol){
        super(protocol);
        this.method = method;
        this.url    = url;    
    }


    public RequestStartLine(String[] tokens){
        this(tokens[0], tokens[1], tokens[2]);
    }


    @Override
    public String toString(){
        return method+" "+url+" "+protocol;
    }


}
