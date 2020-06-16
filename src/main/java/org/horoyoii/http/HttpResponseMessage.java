package org.horoyoii.http;

import java.io.InputStream;
import java.util.StringTokenizer;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.horoyoii.exception.ReadTimeoutException;
import org.horoyoii.http.HttpMessage;
import org.horoyoii.http.startLine.RequestStartLine;
import org.horoyoii.http.startLine.StartLine;
import org.horoyoii.http.startLine.ResponseStatusLine;


@Slf4j
public class HttpResponseMessage extends HttpMessage {


    public HttpResponseMessage(InputStream ins) throws ReadTimeoutException{
        super(ins);
    }

    public HttpResponseMessage() {

    }


    @Override
    StartLine buildStartLine(InputStream inputStream) throws ReadTimeoutException {

        String sb = this.getStartLineBuffer(inputStream);
        String[] tokens = sb.split(" ");

        return new ResponseStatusLine(tokens);
    }


}

