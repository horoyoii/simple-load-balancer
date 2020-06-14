package org.horoyoii.utils;

import java.io.*;

import org.horoyoii.manager.PeerManager;
import org.horoyoii.exception.AlgoNotValidException;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ConfigurationReader {


    /**
     *    Read the configuration file and set the load balancer setting.
     */
    public static void read(String filePath, PeerManager peerManager){
        log.info("read configuration...");
      
        try(
            BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
        ){
            String line = br.readLine();

            while(line != null)
            {    
                if(line.length() != 0 && line.charAt(0) != '#'){ // ignore the comments(#)                     
                    String[] splits = line.split(" ");
                    
                    if(splits[0].equals("algo"))
                    {           
                        try{         
                            peerManager.setLoadBalancingAlgorithm(splits[1]);
                        }catch(AlgoNotValidException a){
                            log.error(a.getMessage());
                            a.soWhat();

                            System.exit(0);     
                        }catch(Exception e){
                            System.exit(0);
                        }

                    }else if(splits[0].equals("server")){
                        peerManager.add(splits[1], splits[2], splits[3], 
                                                    splits.length == 4 ? PeerManager.DEFAULT_WEIGHT : splits[4]);
                    }
                }

                line = br.readLine();
            }

        }catch(FileNotFoundException e){
            log.error(e.toString());
        }catch(IOException i){
            log.error(i.toString()); 
        }catch(Exception e){
            log.error(e.toString());
        }       
   }

}
