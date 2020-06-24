package org.horoyoii.utils;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.horoyoii.manager.PeerManager;
import org.horoyoii.exception.AlgoNotValidException;

import lombok.extern.slf4j.Slf4j;
import com.typesafe.config.*;
import org.horoyoii.model.Location;
import org.horoyoii.router.Router;


@Slf4j
public class ConfigReader {
    private final static String SERVER = "server";
    private final static String UPSTREAM = "upstream";

    private static String rootDir = "/var/www";
    private static int port = 8080;


    public static void read(String filePath, PeerManager peerManager, Router router){
        File file = new File(filePath);

        if(!file.exists()){
            log.info(filePath+" Not Found");
            System.exit(0);
        }

        Config rootConfig = ConfigFactory.parseFile(file);

        readServerContext(rootConfig.getConfig(SERVER), router);
        readUpstreamContext(rootConfig.getConfig(UPSTREAM), peerManager);
    }


    /**
     * Read a server context from the configuration file.
     *
     * @param config
     */
    private static void readServerContext(Config config, Router router){
        if(config.hasPath("listen")){
            port = config.getInt("listen");
        }

        if(config.hasPath("root")){
            rootDir = config.getString("root");
        }


        // iterate each location context and make a location instance.
        System.out.println(config.getConfigList("location"));
        List<Config> cons = (List<Config>) config.getConfigList("location");


        // add this location instance to router.
        for(Config con : cons){
            Location.Builder builder;

            if(con.hasPath("type") && con.hasPath("pattern")){   //TODO : Replace string with constants or enums.
                builder = new Location.Builder(con.getString("type"), con.getString("pattern"));
            }else{
                //TODO : configuration parse Error
                return;
            }

            if(con.hasPath("proxy_pass")){
                builder.proxy_pass(con.getString("proxy_pass"));
            }else if(con.hasPath("root")){
                builder.requestPath(con.getString("root"));
            }

            router.addLocation(builder.build());
        }
    }


    /**
     * Read a upstream context from the configuration file.
     *
     * @param config
     */
    private static void readUpstreamContext(Config config, PeerManager peerManager){
        final String NAME = "name";
        final String ALGO = "algo";
        final String SERVERS = "servers";

        String upstreamName = config.getString(NAME);
        peerManager.setName(upstreamName);

        if(config.hasPath(ALGO)){
            String algoName = config.getString(ALGO);

            try{
                peerManager.setLoadBalancingAlgorithm(algoName);
            }catch(AlgoNotValidException a){
                log.error(a.getMessage());
                a.soWhat();
                System.exit(0);
            }
        }

        for(Map.Entry<String, ConfigValue> entry : config.getConfig(SERVERS).entrySet()){
            String name = entry.getKey();

            //TODO :
            if(entry.getValue().valueType().equals(ConfigValueType.LIST)){
                List<Object> item = (List<Object>)entry.getValue().unwrapped();
                peerManager.addPeer(entry.getKey(), (String)item.get(0), (String)item.get(1), (String)item.get(2));
            }
        }
    }


    public static String getRootDir(){
        return rootDir;
    }


    public static int getPort(){
        return port;
    }
}
