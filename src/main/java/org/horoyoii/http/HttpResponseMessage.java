package org.horoyoii.http;

import java.io.InputStream;
import java.util.StringTokenizer;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.horoyoii.http.HttpMessage;
import org.horoyoii.http.startLine.RequestStartLine;
import org.horoyoii.http.startLine.StartLine;
import org.horoyoii.http.startLine.ResponseStatusLine;


@Slf4j
public class HttpResponseMessage extends HttpMessage {


    public HttpResponseMessage(InputStream ins){
        super(ins);
    }

    public HttpResponseMessage() {

    }


    @Override
    StartLine buildStartLine(InputStream inputStream){
        log.debug("buildStartLine in Response");

        String sb = this.getStartLineBuffer(inputStream);
        log.debug(sb);
        StringTokenizer st = new StringTokenizer(sb, " ");

        String protocol     = st.nextToken();
        String statusCode   = st.nextToken();
        String statusText   = st.nextToken();

        return new ResponseStatusLine(protocol, statusCode, statusText);
    }

}

