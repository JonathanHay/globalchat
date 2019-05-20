/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;
import java.net.*;
import java.io.*;
import java.util.Scanner;
/**
 *
 * @author Jonathan
 */
public class ClientTest {
     public static void main(String[] args) {
         Scanner in = new Scanner(System.in);
         System.out.println("USERNAME: ");
         String username = in.nextLine();
         try{
             Socket sock = new Socket("localhost", 8081);            
           //  ChatClient test = new ChatClient(sock, username);
          //   Thread T = new Thread(test);
        //     T.start();
             while(true){              
                 String message = in.nextLine();
              //   test.send(message);
             }
         }catch(Exception e){
             System.out.println(e);
         }
         
     }
}
