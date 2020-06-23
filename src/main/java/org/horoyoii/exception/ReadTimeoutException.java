package org.horoyoii.exception;

import lombok.Getter;
import org.horoyoii.http.constants.HttpStatus;


/**
 *  504 GATEWAY_TIMEOUT
 *
 */
@Getter
public class ReadTimeoutException extends Exception{
    private HttpStatus httpStatus;


    public ReadTimeoutException(String msg){
        super(msg);
        this.httpStatus = HttpStatus.GATEWAY_TIMEOUT;
    }


}
