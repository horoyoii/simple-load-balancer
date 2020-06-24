package org.horoyoii.model;


import org.horoyoii.http.header.Header;
import org.horoyoii.utils.ConfigReader;

public class Location {
    private static final String FROM_FS = "fromFs";
    private static final String FROM_UP = "fromUpstream";

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
        private final String patternType;
        private final String pattern;

        private String from = Location.FROM_FS;
        private String requestPath = ConfigReader.getRootDir();

        public Builder(String patternType, String pattern){
            this.patternType = patternType;
            this.pattern = pattern;
        }

        public Builder proxy_pass(String address){
            from = Location.FROM_UP;
            //TODO : parse an alias.

            return this;
        }

        public Builder requestPath(String path){

            return this;
        }

        public Location build(){
            return new Location(this);
        }
    }
}
