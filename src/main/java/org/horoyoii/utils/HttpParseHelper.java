package org.horoyoii.utils;

import java.io.IOException;
import java.io.InputStream;

public class HttpParseHelper {

    public static final String END_OF_HEADER = "";
    private static final char   CR = '\r'; // 0x0D
    private static final char   LF = '\n'; // 0x0A


    public static void parseBody(InputStream inputStream, StringBuilder sb){
        int contentsSize = sb.capacity();

        try {
            //TODO :
            for(int i=0; i < contentsSize; i++){
                char readChar = (char)inputStream.read();
                sb.append(readChar);
            }

        } catch (IOException e) {
            // TODO;
        }

    }

    /**
     *
     *
     * @param inputStream
     * @return
     */
    public static String getOneLine(InputStream inputStream){

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
        } catch (IOException e) {
            // TODO;
        }

        return sb.toString();
    }
}
