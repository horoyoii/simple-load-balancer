package org.horoyoii.exception;

import lombok.Getter;
import org.horoyoii.algo.AlgoName;
import org.horoyoii.http.startLine.HttpStatus;


@Getter
public class NoLiveUpstreamException extends Exception{
    private HttpStatus httpStatus;

    public NoLiveUpstreamException(String msg){
        super(msg);
        this.httpStatus = HttpStatus.BAD_GATEWAY;
    }

}
