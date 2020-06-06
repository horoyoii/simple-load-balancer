package org.horoyoii.http.startLine;


/**
 * [method] [url] [protocol/version]
 *
 * ex) POST /user/hello HTTP/1.0
 *
 */
public class RequestStartLine extends StartLine {

    private String method;
    private String url;


    public RequestStartLine(String method, String url, String protocol){
        super(protocol);

        this.method = method;
        this.url    = url;    
    }

    @Override
    public String toString(){
        return method+" "+url+" "+protocol;
    }
}
