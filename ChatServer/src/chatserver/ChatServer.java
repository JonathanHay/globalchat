/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
/**
 *
 * @author Jonathan
 */
public class ChatServer {

    /**
     * @param args the command line arguments
     */
    
    public static ArrayList<Socket> ConnectionArray = new ArrayList<Socket>();
    public static ArrayList<String> CurrentUsers = new ArrayList<String>();
    public static ArrayList<String> Disconnects = new ArrayList<String>();
    
    
    public static void main(String[] args) {
        System.out.println("RUNNING...");
        try{
            final int PORT;
            int tempPort = -1;
            for(String arg:args){
                if(arg.startsWith("-p")){           
                    try{
                        tempPort= Integer.valueOf(arg.split(" ")[1]);
                    }catch(Exception e){
                        System.out.println("Invalid port format (Must be integer)");
                    }
                }
            }           
            if(tempPort > -1){
                PORT = tempPort;               
            }else{
                PORT = 8081;
            }
            
            ServerSocket SERVER = new ServerSocket(PORT);
            System.out.println("Waiting for clients to connect...");
            
            while(true){
                Socket sock = SERVER.accept();
                ConnectionArray.add(sock);
                
                System.out.println("Client connected from source IP " + sock.getLocalAddress().getHostName());
                
                AddUser(sock);
            
                Instance chat = new Instance(sock);
                
                Thread t = new Thread(chat);
                t.start();
            }
            
        }catch(Exception e){
            System.out.println("main");
            System.out.println(e);
        }
    }

    private static void AddUser(Socket SOCK) throws IOException{
        Scanner INPUT = new Scanner(SOCK.getInputStream());
        String UserName = INPUT.nextLine();
        
        CurrentUsers.add(UserName);
                      
        for(int i = 0; i <  ConnectionArray.size(); i++){
            Socket temp = ConnectionArray.get(i);
            if(!temp.isClosed()){    
                PrintWriter output = new PrintWriter(temp.getOutputStream());
                output.println("%/" + CurrentUsers);
                output.flush();
            }
        }  
    }   
}

class Instance implements Runnable{
    private final Socket _SOCK;
    private Scanner _in;
    private PrintWriter _out;
    private String _response = "";
    public Instance(Socket SOCK){
        this._SOCK = SOCK;
    }
    
    
    @Override
    public void run() {
        try{
            
            try{
            
                _in = new Scanner(_SOCK.getInputStream());
                _out = new PrintWriter(_SOCK.getOutputStream());
                
                while(true){  
                    
                    checkConn();               
                    if(!_in.hasNext()){
                        return;
                    }                                                 
                    _response = _in.nextLine();
                    System.out.println("Client sent response: " + _response);
                    
                    for(int i = 0; i < ChatServer.ConnectionArray.size(); i++){
                        Socket sock = ChatServer.ConnectionArray.get(i);
                        if(!sock.isClosed()){
                            PrintWriter out = new PrintWriter(sock.getOutputStream());
                            if(_response.startsWith("/w")){
                                String rec = _response.substring(3).split("]")[0];
                                String temp = "[Whisper]" + _response.substring(3).split("]")[1];
                                if(ChatServer.CurrentUsers.get(i).equals(rec)){
                                    out.println(temp);
                                    System.out.println("Sent to: " + sock.getLocalAddress().getHostName() + "(" + ChatServer.CurrentUsers.get(i) + ")");
                                }                             
                            }else{
                               out.println(_response); 
                               System.out.println("Sent to: " + sock.getLocalAddress().getHostName());
                            }                          
                            out.flush();
                            
                        }
                    }
                }
                
            }finally{
                System.out.println("closing socket");
                _SOCK.close();
                checkConn();
                 
            }
        }catch(Exception e){
            System.out.println("run");
            System.out.println(e);
        }
    }
    
    private static void checkConn() throws IOException{
        for(int i = 0; i <  ChatServer.ConnectionArray.size(); i++){
            Socket temp = ChatServer.ConnectionArray.get(i);
            if(temp.isClosed()){
                ChatServer.ConnectionArray.remove(i);
                ChatServer.Disconnects.add(ChatServer.CurrentUsers.remove(i));
            }
        } 
        if(!ChatServer.Disconnects.isEmpty()){
            for(int i = 0; i <  ChatServer.ConnectionArray.size(); i++){             
                Socket temp = ChatServer.ConnectionArray.get(i);
                if(!temp.isClosed()){
                    PrintWriter out = new PrintWriter(temp.getOutputStream());
                    for(int k = 0; k < ChatServer.Disconnects.size(); k++){
                        out.println(ChatServer.Disconnects.get(k) + " has disconnected!");                     
                    }  
                    out.flush();  
                }
            } 
          ChatServer.Disconnects.clear();
        }
    }
    
}
