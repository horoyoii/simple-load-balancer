package org.horoyoii.http.startLine;

import lombok.Getter;


@Getter
public enum HttpStatus {
    BAD_GATEWAY(502, "Bad Gateway");

    int statusCode;
    String statusText;

    HttpStatus(int statusCode, String statusText){
        this.statusCode = statusCode;
        this.statusText = statusText;
    }

}
