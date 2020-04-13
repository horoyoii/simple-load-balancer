import java.net.*;
import java.io.*;


class Server{

    public static void main(String[] args){

       int port = Integer.parseInt(args[0]);
       System.out.println("Server started on "+port);         

       try{ 
            ServerSocket sock = new ServerSocket(port);  
            while(true){ 
            
                Socket cli = sock.accept();
                
                InputStream is = cli.getInputStream();
                OutputStream os = cli.getOutputStream();
    
                System.out.println("client comes!!");
                
                byte[] data = new byte[1000];
                
                int readByte;
                
                is.read(data); 
    
                String rec = new String(data);
                
                System.out.println("recv : "+rec);   
                
                os.write("Bye from server".getBytes());

                os.close();
                is.close();
                cli.close();
                System.out.println("serving done");
            }   

        }catch(Exception e){
            System.out.println(e);
        }

    }

}
