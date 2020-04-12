package org.horoyoii.connection;

import java.net.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;


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
        Thread.State st = Thread.currentThread().getState();
        log.info(st.toString());        
        
        try{
            Thread.sleep(1000);   
        }catch(Exception e){

        }

        con.getState();

        byte[] buffer = new byte[BUFFER_SIZE];
        
        try{
            int byteRead = in.read(buffer);   
            
            if(byteRead == -1){ // EOF which means close()
                //TODO

            }

            out.write(buffer, 0, byteRead);
            out.flush();
        }catch(IOException e){
            
        }
    }

}



