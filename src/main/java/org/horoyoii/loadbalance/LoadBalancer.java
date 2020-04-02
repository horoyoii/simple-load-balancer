package org.horoyoii.loadbalance;

import java.io.*;
import org.horoyoii.distribution.ServerDistributor;

public class LoadBalancer{
    ServerDistributor serverDistributor = new ServerDistributor();
    

    public void readConfig(){
        System.out.println("read conf");
        
        String path = "/home/horoyoii/Desktop/simple-load-balancer/load.config";
        
        File f              = null;
        FileReader fr       = null;
        BufferedReader br   = null;
        
        try{
            f = new File(path);
            fr = new FileReader(f);
            br = new BufferedReader(fr);
 
            String line = br.readLine();
            while(line != null){
                  
                System.out.println(line);
                line = br.readLine();
            }
        }catch(FileNotFoundException e){
            System.err.println(e);
        }catch(IOException i){
            System.err.println(i);   
        }finally{
            
            try{   
                fr.close();
                br.close();
            }catch(Exception e){
                System.err.println(e);
            }
        }
    
        
    }

    public void run(){
        System.out.println("Runnin...");
    }

}

