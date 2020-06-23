package org.horoyoii.utils;

import java.io.*;
import java.util.List;
import java.util.Map;

import org.horoyoii.manager.PeerManager;
import org.horoyoii.exception.AlgoNotValidException;

import lombok.extern.slf4j.Slf4j;
import com.typesafe.config.*;


@Slf4j
public class ConfigurationReader {
    private final static String SERVER = "server";
    private final static String UPSTREAM = "upstream";


    public static void read(String filePath, PeerManager peerManager){
        File file = new File(filePath);

        if(!file.exists()){
            log.info(filePath+" Not Found");
            System.exit(0);
        }

        Config rootConfig = ConfigFactory.parseFile(file);

        readServerContext(rootConfig.getConfig(SERVER));
        readUpstreamContext(rootConfig.getConfig(UPSTREAM), peerManager);
    }


    /**
     * Read a server context from the configuration file.
     *
     * @param config
     */
    private static void readServerContext(Config config){


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

        String upstreamName = (String) config.getValue(NAME).unwrapped();
        peerManager.setName(upstreamName);

        if(config.hasPath("algo")){
            String algoName = (String)config.getValue(ALGO).unwrapped();

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


//    /**
//     *    Read the configuration file and set the load balancer setting.
//     */
//    public static void read0(String filePath, PeerManager peerManager){
//        log.info("read configuration...");
//
//        try(
//            BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
//        ){
//            String line = br.readLine();
//
//            while(line != null)
//            {
//                if(line.length() != 0 && line.charAt(0) != '#'){ // ignore the comments(#)
//                    String[] splits = line.split(" ");
//
//                    if(splits[0].equals("algo"))
//                    {
//                        try{
//                            peerManager.setLoadBalancingAlgorithm(splits[1]);
//                        }catch(AlgoNotValidException a){
//                            log.error(a.getMessage());
//                            a.soWhat();
//
//                            System.exit(0);
//                        }catch(Exception e){
//                            System.exit(0);
//                        }
//
//                    }else if(splits[0].equals("server")){
//                        peerManager.addPeer(splits[1], splits[2], splits[3],
//                                                    splits.length == 4 ? PeerManager.DEFAULT_WEIGHT : splits[4]);
//                    }
//                }
//
//                line = br.readLine();
//            }
//
//        } catch(Exception e){
//            log.error(e.toString());
//        }
//    }


}
