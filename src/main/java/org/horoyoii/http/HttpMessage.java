package org.horoyoii.http;

import java.util.Map;
import java.io.InputStream;

import org.horoyoii.http.startLine.StartLine;

public abstract class HttpMessage {

    StartLine startLine;
    
    Map<String, String> headers;

    String body;

    
    public HttpMessage(InputStream ins){
        startLine = buildStartLine(ins);
        
        buildHeader(ins);

        buildBody(ins);
    }


    abstract StartLine buildStartLine(InputStream ins);

   
    void buildHeader(InputStream inputStream){

    }

    
    void buildBody(InputStream inputStream){

    }

}
