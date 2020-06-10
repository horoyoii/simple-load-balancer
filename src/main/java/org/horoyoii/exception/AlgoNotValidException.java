package org.horoyoii.exception;


import lombok.extern.slf4j.Slf4j;
import org.horoyoii.algo.AlgoName;

@Slf4j
public class AlgoNotValidException extends Exception{
    
    public AlgoNotValidException(String msg){
        super(msg); 
    }

    public void soWhat(){
        log.info("Valid names of algorithms are...");
        StringBuilder sb = new StringBuilder().append("( ");

        for(AlgoName algoName : AlgoName.values())
            sb.append(algoName.getName()).append(", ");

        log.info(sb.append(")").toString());
    }
}
