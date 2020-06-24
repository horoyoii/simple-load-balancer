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
 */
@Slf4j
public class Router {
    private List<Location> exactLocationList;
    private List<Location> regexLocationList;
    private List<Location> prefixLocationList;


    public Router(){
        exactLocationList = new ArrayList<>();
        regexLocationList = new ArrayList<>();
        prefixLocationList = new ArrayList<>();
    }


    public void addLocation(Location location){
        log.debug(location.toString());
        //locationList.add(location);
    }


    public String determineWhereTo(String uri){
        // iterate a location list
        return "directory";
    }

}
