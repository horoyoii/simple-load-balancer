package org.horoyoii.http;

import java.io.InputStream;
import java.util.StringTokenizer;

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
        StringTokenizer st = new StringTokenizer(sb, " ");

        //TODO : try-catch for parse error 
        String method   = st.nextToken();
        String url      = st.nextToken();
        String protocol = st.nextToken();
       

        return new RequestStartLine(method, url, protocol);
    }

    public String getURL(){
        return ((RequestStartLine)startLine).getUrl();
    }
}
