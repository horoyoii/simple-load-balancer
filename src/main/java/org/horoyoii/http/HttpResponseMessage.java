package org.horoyoii.http;

import java.io.InputStream;
import org.horoyoii.http.HttpMessage;
import org.horoyoii.http.startLine.StartLine;
import org.horoyoii.http.startLine.ResponseStatusLine;


class HttpResponseMessage extends HttpMessage {


    public HttpResponseMessage(InputStream ins){
        super(ins);
         
    }


    @Override
    StartLine buildStartLine(InputStream ins){
        //TODO
        
        return new ResponseStatusLine("mock", "mock", "mock");      
    }


}

