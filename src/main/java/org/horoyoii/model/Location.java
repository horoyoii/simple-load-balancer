package org.horoyoii.model;


import lombok.Getter;
import org.horoyoii.utils.ConfigReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 *
 *
 */
@Getter
public class Location {
    public static final String FROM_FS = "fromFs";
    public static final String FROM_UPSTREAM = "fromUpstream";

    public static final String EXACT   = "exact";
    public static final String REGEX   = "regex";
    public static final String PREFIX  = "prefix";

    private final String from;

    /*
     * exact or regex or prefix type.
     */
    private final String patternType;

    private final String pattern;

    /*
     * this can be a directory path or upstream server address.
     */
    private final String requestPath;

    //Header header;

    public Location(Builder builder){
        this.patternType = builder.patternType;
        this.pattern = builder.pattern;
        this.requestPath = builder.requestPath;
        this.from = builder.from;
    }


    public static class Builder {
        private static final Pattern PATTERN = Pattern.compile("^(.+)://(.+)/$");

        private final String patternType;
        private final String pattern;

        private String from = Location.FROM_FS;
        private String requestPath = ConfigReader.getRootDir();


        public Builder(String patternType, String pattern){
            this.patternType = patternType;
            this.pattern = pattern;
        }


        public Builder proxy_pass(String address){
            from = Location.FROM_UPSTREAM;
            Matcher m =  PATTERN.matcher(address);

            if (m.find()) {
                this.requestPath = m.group(2);
            }

            return this;
        }


        public Builder requestPath(String path){
            this.requestPath = path;
            return this;
        }


        public Location build(){
            return new Location(this);
        }
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("patternType : ").append(patternType).append("\n");
        sb.append("uri pattern : ").append(pattern).append("\n");
        sb.append("from : ").append(from).append("\n");
        sb.append("request path : ").append(requestPath).append("\n");

        return sb.toString();
    }
}
