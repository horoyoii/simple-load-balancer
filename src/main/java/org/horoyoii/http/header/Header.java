package org.horoyoii.http.header;

import org.horoyoii.utils.HttpParseHelper;

import javax.swing.text.html.HTML;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * In HTTP, Header is optional.
 *
 *
 */
public class Header {

    private Map<String, String> headers = new HashMap<>();

    public Header(InputStream inputStream){
        String kv;

        while( !(kv = HttpParseHelper.getOneLine(inputStream)).equals(HttpParseHelper.END_OF_HEADER)) {
            int idx = kv.indexOf(":");

            if(idx == -1){
                return;
            }

            String key = kv.substring(0, idx).toLowerCase().trim();
            String value = kv.substring(idx+1).toLowerCase().trim();

            headers.put(key, value);
        }
    }

    /**
     * @param key
     * @return
     *      if not exist, return null.
     */
    public String getHeader(String key){
        return headers.get(key.toLowerCase().trim());
    }



    /**
     * Add header.
     *  If this already exists, then overwrites it.
     *
     * @param key
     * @param value
     */
    public void setHeader(HeaderDirective key, HeaderDirective value){
        headers.put(key.getDirective(), value.getDirective());
    }



    @Override
    public String toString(){
        if(headers.isEmpty()) return null;

        StringBuilder sb = new StringBuilder();

        for(Map.Entry<String, String> entry : headers.entrySet()){
            sb.append(entry.getKey());
            sb.append(": ");
            sb.append(entry.getValue());
            sb.append(HttpParseHelper.CRLF);
        }
        sb.append(HttpParseHelper.CRLF);

        return sb.toString();
    }

}
