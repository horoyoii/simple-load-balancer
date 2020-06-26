package org.horoyoii.router;


import lombok.extern.slf4j.Slf4j;
import org.horoyoii.model.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Determine which location should serve a client request.
 *
 * Priority
 * 1. exact match
 *  location = { }
 * 2. regex match
 *  location ~ { }
 * 3. prefix match
 * location { }
 *
 */
@Slf4j
public class Router {
    private List<Location> exactLocationList;
    private List<Location> regexLocationList;
    private List<Location> prefixLocationList;

    private Location defaultLocation;


    public Router(){
        exactLocationList = new ArrayList<>();
        regexLocationList = new ArrayList<>();
        prefixLocationList = new ArrayList<>();
    }


    public void addLocation(Location location){
        if(location.getPatternType().equals(Location.EXACT)){
            exactLocationList.add(location);
        }else if(location.getPatternType().equals(Location.REGEX)){
            regexLocationList.add(location);
        }else if(location.getPatternType().equals(Location.PREFIX)){
            prefixLocationList.add(location);
        }else{
            //TODO: error occurs
        }
    }


    /**
     *
     *
     * @param uri
     * @return
     */
    public Location getLocation(String uri){
        log.debug(uri);

        for(Location location : exactLocationList){
            String pattern = location.getPattern();
            log.debug(pattern);
            if (pattern.equals(uri)) {
                return location;
            }
        }


        for(Location location : regexLocationList){
            String pattern = location.getPattern();
            log.debug(pattern);
            if(uri.matches(pattern)){
                return location;
            }
        }


        for(Location location : prefixLocationList){
            String pattern = location.getPattern();
            if(uri.startsWith(pattern)){
                return location;
            }
        }


        return null;
    }


    public void createDefaultLocation(String uri){

    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("exact location list\n");
        for(Location location : exactLocationList){
            sb.append(location);
        }

        sb.append("regex location list\n");
        for(Location location : regexLocationList){
            sb.append(location);
        }

        sb.append("prefix location list\n");
        for(Location location : prefixLocationList){
            sb.append(location);
        }

        return sb.toString();
    }
}
