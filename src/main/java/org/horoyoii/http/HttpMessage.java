package org.horoyoii.http;

import java.util.Map;
import java.io.InputStream;
import java.io.IOException;

import org.horoyoii.http.startLine.StartLine;

public abstract class HttpMessage {

    private final static char CR = '\r';    // 0x0D
    private final static char LF = '\n';    // 0x0A

    StartLine startLine;
    
    Map<String, String> headers;

    String body;

    
    public HttpMessage(InputStream ins){
        startLine = buildStartLine(ins);
        System.out.println(startLine);        
        
        buildHeader(ins);
        buildBody(ins);
    }
    
    abstract StartLine buildStartLine(InputStream ins);

   
    /**
     * 
     */
    StringBuffer getStartLineBuffer(InputStream inputStream){
        StringBuffer sb = new StringBuffer();

        int readByte = -1;

        try{
            
            while((readByte = inputStream.read()) != -1){
                char readChar = (char)readByte;
                
                if(readChar == LF){
                    break;
                }else{
                    if(readChar == CR){
                        continue;
                    }
                    sb.append(readChar);
                }                        
            }   
        }
        catch(IOException e){
            //TODO;
        }
        
        return sb;
    }

   
    void buildHeader(InputStream inputStream){

    }

    
    void buildBody(InputStream inputStream){

    }

}
