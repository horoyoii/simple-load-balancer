package org.horoyoii.http;

import java.io.InputStream;

import lombok.extern.slf4j.Slf4j;
import org.horoyoii.exception.ReadTimeoutException;
import org.horoyoii.http.HttpMessage;
import org.horoyoii.http.startLine.StartLine;
import org.horoyoii.http.startLine.RequestStartLine;

@Slf4j
public class HttpRequestMessage extends HttpMessage {


    public HttpRequestMessage(InputStream ins) throws ReadTimeoutException {
        super(ins);

    }
   
    
    @Override
    StartLine buildStartLine(InputStream inputStream) throws ReadTimeoutException{
        
        String sb = this.getStartLineBuffer(inputStream);
        String[] tokens = sb.split(" ");

        //TODO : try-catch for parse error
        return new RequestStartLine(tokens);
    }


    public String getURL(){
        return ((RequestStartLine)startLine).getUrl();
    }


}
