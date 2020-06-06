package org.horoyoii.http.startLine;

import org.horoyoii.utils.HttpParseHelper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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
     *
     * @param key
     * @return
     *      if not exist, return null.
     */
    public String getHeader(String key){
        return headers.get(key.toLowerCase().trim());
    }
}
