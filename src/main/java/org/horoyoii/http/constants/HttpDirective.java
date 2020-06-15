package org.horoyoii.http.constants;

public enum HttpDirective {

    // 컨텐츠 협상 ================================================
    CONTENT_LENGTH("content-length"),
    CONTENT_TYPE("content-type"),
    TRANSFER_ENCODING("transfer-encoding"),


    // 커넥션 =====================================================
    CONNECTION("connection"),

    CLOSE("close"),

    KEEPALIVE("keep-alive"),


    // =========================================================
    DATE("Date"),
    SERVER("Server");


    // 캐시 컨트롤 =================================================

    String directive;

    HttpDirective(String directive){
        this.directive = directive;
    }

    public String getDirective(){
        return directive;
    }
}
