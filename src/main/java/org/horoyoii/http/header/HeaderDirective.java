package org.horoyoii.http.header;

public enum HeaderDirective {

    // 컨텐츠 협상 ================================================
    CONTENT_LENGTH("content-length"),
    CONTENT_TYPE("content-type"),
    TRANSFER_ENCODING("transfer-encoding"),


    // 커넥션 =====================================================
    CONNECTION("connection"),

    CLOSE("close"),

    KEEPALIVE("keep-alive");



    // 캐시 컨트롤 =================================================

    String directive;

    HeaderDirective(String directive){
        this.directive = directive;
    }

    public String getDirective(){
        return directive;
    }
}
