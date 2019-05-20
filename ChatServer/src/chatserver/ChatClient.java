/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Jonathan
 */
public class ChatClient implements Runnable {
    private final Socket _sock;
    private Scanner _in;
    private Scanner _msg = new Scanner(System.in);
    private PrintWriter _out;
    private String _username;
    private String _online [];
    
    public ChatClient(Socket sock, String username){
        _sock = sock;
        _username = username;
    }

    @Override
    public void run() {
        try{
            try{
                _in = new Scanner(_sock.getInputStream());
                _out = new PrintWriter(_sock.getOutputStream());
                _out.flush();
                CheckStream();
            }finally{
                _sock.close();
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public void disconnect() throws IOException{
        _out.println(_username + " has disconnected");
        _out.flush();
        _sock.close();
        System.exit(0);
    }
    
    private void CheckStream() {
        while(true){
            recieve();
        }
    }

    private void recieve() {
        if(_in.hasNext()){
            
            String msg = _in.nextLine();         
            if(msg.contains("%/")){
                String temp = msg.substring(2);
                temp = temp.replace("{","");
                temp = temp.replace("}","");
                
                String[] users = temp.split(", ");
                if(users.length > 0){
                    for(String x:users){
                    MainGUI.chatBox.append(x + ", ");       
                    System.out.print(x + ", ");
                }  
                MainGUI.chatBox.append(" Are online \n"); 
                System.out.println("are online");
                }else{
                    System.out.println("No users online");
                }              
            }
            else{
                MainGUI.chatBox.append(msg + "\n");
                System.out.println(msg);
            }
        }
    }
    
    public void send(String s){
        
        try {
            if(s.contains("/w")){
                String temp = s.substring(2);
                String to[] = temp.split(" ");
                int correctIndex = 1;
                for(int i = 0; i < to.length; i++){
                    if(to[i].equals("/w")){
                        correctIndex = i + 1;
                        break;
                    }
                }    
                temp = temp.substring(to[correctIndex].length() + 2);
                _out.println("/w" + "["+ to[correctIndex] + "]" + _username + ": " + temp);
            }else{
                _out.println(_username + ": " + s);
            }       
             _out.flush();
        } catch (Exception ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void connect(){
         try {
             _out.println(_username);
             _out.flush();
        } catch (Exception ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
