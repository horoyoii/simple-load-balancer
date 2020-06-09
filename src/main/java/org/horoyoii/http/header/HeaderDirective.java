package org.horoyoii.http.header;

public enum HeaderDirective {

    CONTENT_LENGTH("content-length"),


    CONNECTION("connection"),

    CLOSE("close"),

    KEEPALIVE("keep-alive");


    String directive;

    HeaderDirective(String directive){
        this.directive = directive;
    }

    public String getDirective(){
        return directive;
    }
}
