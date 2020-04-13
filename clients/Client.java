import java.net.*;
import java.io.*;

class Client{
    private static int id;

    public static void main(String[] args){
        
        if(args.length != 2){
            log("no args");
            System.exit(1);   
        }
        
        int port = Integer.parseInt(args[0]);
        id = Integer.parseInt(args[1]);

        try{
            Socket socket = new Socket("127.0.0.1", port);
            
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            String data = "ABCD";

            out.write(data.getBytes());
            
            log("send done");
            
        
            int resByte = 0;
            byte[] recData = new byte[1024];
            
            
            while((resByte = in.read(recData)) != -1){
            
                log("read done");
    
                String rec = new String(recData);
    
                log("result : "+rec);
            }
  
            out.close();
            in.close();
            socket.close();
                        
        }catch(Exception e){
            log(e.toString());
        }
        
        System.out.println("DONE : exit application");
    }

    public static void log(String msg){
        System.out.println("[INFO - "+id+" ] : "+msg);
    }

}
