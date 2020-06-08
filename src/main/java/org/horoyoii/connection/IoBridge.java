package org.horoyoii.connection;

import java.net.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;


/**
 *  Not used
 *
 *
 */
@Slf4j
class IoBridge implements Runnable{
    private static final int BUFFER_SIZE = 1024;

    private InputStream     in;
    private OutputStream    out;
    private Connection      con;
    
    IoBridge(Connection con, InputStream in, OutputStream out){
        this.in  = in;
        this.out = out;
        this.con = con;
    }

    @Override
    public void run(){
        byte[] buffer = new byte[BUFFER_SIZE];
        
        try{
            while(true){

                int byteRead = in.read(buffer);   
                log.info("read {} byte and waiting...", byteRead);
                if(byteRead == -1){ // EOF which means close()
                    //TODO
                    break; 
                }
                
                out.write(buffer, 0, byteRead);
                out.flush();
            }
        }catch(IOException e){
            log.error(e.toString());
        }


    }
}



