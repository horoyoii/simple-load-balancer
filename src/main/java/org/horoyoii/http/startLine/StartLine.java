package org.horoyoii.http.startLine;


public abstract class StartLine{

    protected String protocol;
    
    StartLine(String protocol){
        this.protocol = protocol;
    }

}
