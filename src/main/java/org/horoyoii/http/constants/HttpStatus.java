package org.horoyoii.http.constants;

import lombok.Getter;


@Getter
public enum HttpStatus {
    BAD_GATEWAY(502, "Bad Gateway"),
    GATEWAY_TIMEOUT(504, "Gateway Timeout");

    int statusCode;
    String statusText;

    HttpStatus(int statusCode, String statusText){
        this.statusCode = statusCode;
        this.statusText = statusText;
    }

}
