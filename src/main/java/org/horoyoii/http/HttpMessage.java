package org.horoyoii.http;

import java.util.Map;
import org.horoyoii.http.startLine.StartLine;

public abstract class HttpMessage {

    private StartLine startLine;
    
    private Map<String, String> headers;

    private String body;

    
}
