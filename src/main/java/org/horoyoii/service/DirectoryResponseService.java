package org.horoyoii.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;

import org.horoyoii.http.HttpRequestMessage;
import org.horoyoii.http.HttpResponseMessage;
import org.horoyoii.http.constants.HttpDirective;
import org.horoyoii.http.constants.HttpStatus;
import org.horoyoii.http.header.Header;
import org.horoyoii.http.startLine.ResponseStatusLine;
import org.horoyoii.model.Location;
import org.horoyoii.utils.HttpErrorRespHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Create a HTTP response based on a file system.
 *
 */
@Slf4j
public class DirectoryResponseService implements ResponseService{

    private final String rootDirectory;
    private final String filePath;


    public DirectoryResponseService(Location location, String filePath){
        this.rootDirectory = location.getRequestPath();
        this.filePath = filePath;
    }


    /**
     * Get a result from file system.
     *
     */
    @Override
    public HttpResponseMessage getHttpResponseMessage() {
        File file = new File(rootDirectory+filePath);
        if(!file.exists()){
            return HttpErrorRespHandler.getErrorResponse(HttpStatus.NOT_FOUND);
        }

        int fileLength = (int)file.length();


        // read a byte from file system
        byte[] data = readFileData(file, fileLength);


        // make a http response message.
        HttpResponseMessage httpResponseMessage = new HttpResponseMessage();


        //TODO : Factory pattern
        httpResponseMessage.setStartLine(new ResponseStatusLine("HTTP/1.1",
                String.valueOf(HttpStatus.OK.getStatusCode()), String.valueOf(HttpStatus.OK.getStatusText())));

        Header header = new Header();
        header.setHeader(HttpDirective.DATE, getCurrentTime());
        header.setHeader(HttpDirective.SERVER, getServerInfo());


        try{
            header.setHeader(HttpDirective.CONTENT_TYPE, getMimeType(file));
        }catch (IOException e){
            //TODO:
            log.error(e.toString());
        }

        header.setHeader(HttpDirective.CONTENT_LENGTH, fileLength);
        httpResponseMessage.setHeader(header);

        // read data from file.
        httpResponseMessage.setBody(ByteBuffer.wrap(data));

        return httpResponseMessage;
    }


    private byte[] readFileData(File file, int fileLength){
        byte[] data = new byte[fileLength];

        try(
                FileInputStream fis = new FileInputStream(file);) {
            fis.read(data);
        }catch(IOException e){
            log.error(e.toString());
        }

        return data;
    }


    private String getMimeType(File file) throws IOException {
        Tika tika = new Tika();
        return tika.detect(file);
    }


    //TODO : duplicated
    private static String getCurrentTime(){
        ZonedDateTime znt = ZonedDateTime.now(ZoneId.of("UTC"));
        return znt.format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }


    private static String getServerInfo(){
        String n = "HginX/0.1";
        String os = System.getProperty("os.name");
        return n + " ("+os+")";
    }

}
