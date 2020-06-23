package org.horoyoii.exception;

import lombok.Getter;
import org.horoyoii.http.constants.HttpStatus;


/**
 *  502 BAD_GATEWAY
 *
 */
@Getter
public class NoLiveUpstreamException extends Exception{
    private HttpStatus httpStatus;


    public NoLiveUpstreamException(String msg){
        super(msg);
        this.httpStatus = HttpStatus.BAD_GATEWAY;
    }


}
