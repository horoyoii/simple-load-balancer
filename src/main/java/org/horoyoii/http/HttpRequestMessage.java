package org.horoyoii.http;

import java.io.InputStream;
import org.horoyoii.http.HttpMessage;
import org.horoyoii.http.startLine.StartLine;
import org.horoyoii.http.startLine.RequestStartLine;


public class HttpRequestMessage extends HttpMessage {

    public HttpRequestMessage(InputStream ins){
        super(ins);

    }
   
    
    @Override
    StartLine buildStartLine(InputStream ins){
        //TODO
            
        return new RequestStartLine();
    }

}
