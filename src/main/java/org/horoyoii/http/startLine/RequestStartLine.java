package org.horoyoii.http.startLine;

/**
 * [method] [url] [protocol/version]
 *
 * ex) POST /user/hello HTTP/1.0
 *
 */
public class RequestStartLine extends StartLine {

    private String method;
    private String requestTarget;


    public RequestStartLine(String method, String requestTarget, String protocol){
        super(protocol);
        this.method = method;
        this.requestTarget = requestTarget;
    }


    public RequestStartLine(String[] tokens){
        this(tokens[0], tokens[1], tokens[2]);
    }


    @Override
    public String toString(){
        return method+" "+ requestTarget +" "+protocol;
    }


    public String getRequestTarget() {
        return requestTarget;
    }


    public void setRequestTarget(String requestTarget) {
        this.requestTarget = requestTarget;
    }
}
