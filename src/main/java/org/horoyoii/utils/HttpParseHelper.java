package org.horoyoii.utils;

import lombok.extern.slf4j.Slf4j;
import org.horoyoii.exception.ReadTimeoutException;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;

@Slf4j
public class HttpParseHelper {

    public static final String END_OF_HEADER = "";

    public static final char   CR = '\r'; // 0x0D
    public static final char   LF = '\n'; // 0x0A

    public static final String CRLF = "\r\n";


    public static void parseBody(InputStream inputStream, ByteBuffer sb) throws ReadTimeoutException{
        int contentsSize = sb.capacity();

        try {
            //TODO :
            for(int i=0; i < contentsSize; i++){
                int readB = inputStream.read();
                sb.put((byte)readB);
            }

        }catch (SocketTimeoutException e){
            throw new ReadTimeoutException("h...");
        }
        catch (IOException e) {
            // TODO;
        }
    }


    /**
     *
     *
     * @param inputStream
     * @return
     */
    public static String getOneLine(InputStream inputStream) throws ReadTimeoutException {
        StringBuffer sb = new StringBuffer();

        int readByte = -1;

        try {

            while ((readByte = inputStream.read()) != -1) {
                char readChar = (char) readByte;
                if (readChar == LF) {
                    break;
                } else {
                    if (readChar == CR) {
                        continue;
                    }
                    sb.append(readChar);
                }
            }
        }catch(SocketTimeoutException s){
            throw new ReadTimeoutException("h....");
        }
        catch (IOException e) {
            // TODO;
            log.error(e.toString());
        }

        return sb.toString();
    }


}
