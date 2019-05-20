/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainGUI extends JFrame{

    String      appName     = "OS Chat";
    MainGUI     mainGUI;
    JFrame      newFrame    = new JFrame(appName);
    JButton     sendMessage;
    JTextField  messageBox;
    public static JTextArea   chatBox;
    JTextField  usernameChooser;
    JTextField  server;
    JTextField  port;
    JFrame      preFrame;
    String  username;
    String  ip;
    int portNum;
    Socket sock; 
    ChatClient test;
    
    
    public static void main(String[] args) {
        MainGUI mainGUI = new MainGUI();
        
      

        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                try {
                   
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
               
               // sock = new Socket("localhost", 8080);   
                mainGUI.preDisplay();
            }
        });
        
        
       
    }

    public void preDisplay() {
        
      
        
        
        newFrame.setVisible(false);
        preFrame = new JFrame(appName);
        
        
            preFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                
                    System.exit(0);
                
            }
        });
        
        usernameChooser = new JTextField(15);
        server = new JTextField(15);
        port = new JTextField(15);
        
        JLabel chooseServer = new JLabel("Enter Server IP:");
         JLabel choosePort = new JLabel("Enter Server Port:");
         
         server.setText("localhost");
         port.setText("8081");
         
        JLabel chooseUsernameLabel = new JLabel("Pick a username:");
        JButton enterServer = new JButton("Enter Chat Server");
        enterServer.addActionListener(new enterServerButtonListener());
        JPanel prePanel = new JPanel(new GridBagLayout());

        GridBagConstraints preRight = new GridBagConstraints();
        preRight.insets = new Insets(0, 0, 0, 10);
        preRight.anchor = GridBagConstraints.EAST;
        GridBagConstraints preLeft = new GridBagConstraints();
        preLeft.anchor = GridBagConstraints.WEST;
        preLeft.insets = new Insets(0, 10, 0, 10);
        // preRight.weightx = 2.0;
        preRight.fill = GridBagConstraints.HORIZONTAL;
        preRight.gridwidth = GridBagConstraints.REMAINDER;

        GridBagConstraints preTopRight = new GridBagConstraints();
        preTopRight.insets = new Insets(0, 0, 80, 10);
        preTopRight.anchor = GridBagConstraints.EAST;
        GridBagConstraints preTopLeft = new GridBagConstraints();
        preTopLeft.anchor = GridBagConstraints.WEST;
        preTopLeft.insets = new Insets(0, 10, 80, 10);
        // preRight.weightx = 2.0;
       preTopRight.fill = GridBagConstraints.HORIZONTAL;
        preTopRight.gridwidth = GridBagConstraints.REMAINDER;
        GridBagConstraints right = new GridBagConstraints();
        right.insets = new Insets(30, 0, 0, 10);
        right.anchor = GridBagConstraints.EAST;
        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.WEST;
        left.insets = new Insets(30, 10, 0, 10);
        
        prePanel.add(chooseUsernameLabel, preLeft);
        prePanel.add(usernameChooser, preRight);
        prePanel.add(chooseServer, preTopLeft);
        prePanel.add(server, preTopRight);
        prePanel.add(choosePort, left);
        prePanel.add(port, right);
        preFrame.add(BorderLayout.CENTER, prePanel);
        preFrame.add(BorderLayout.SOUTH, enterServer);
        preFrame.setSize(300, 300);
        preFrame.setVisible(true);

    }

    public void display() {
        
        
          newFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
          /*      try {
                 //   test.disconnect();
                        System.exit(0);
                } catch (IOException ex) {
                    System.out.println(ex);
                }*/
                
                 System.exit(0);
            }
        });
        
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel southPanel = new JPanel();
       
        southPanel.setLayout(new GridBagLayout());

        messageBox = new JTextField(30);
        messageBox.requestFocusInWindow();

        sendMessage = new JButton("Send Message");
        sendMessage.addActionListener(new sendMessageButtonListener());

        chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
        chatBox.setLineWrap(true);

        mainPanel.add(new JScrollPane(chatBox), BorderLayout.CENTER);

        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.LINE_START;
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 512.0D;
        left.weighty = 1.0D;

        GridBagConstraints right = new GridBagConstraints();
        right.insets = new Insets(0, 10, 0, 0);
        right.anchor = GridBagConstraints.LINE_END;
        right.fill = GridBagConstraints.NONE;
        right.weightx = 1.0D;
        right.weighty = 1.0D;

        southPanel.add(messageBox, left);
        southPanel.add(sendMessage, right);

        mainPanel.add(BorderLayout.SOUTH, southPanel);

        newFrame.add(mainPanel);
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setSize(470, 300);
        newFrame.setVisible(true);
    }

    class sendMessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (messageBox.getText().length() < 1) {
                // do nothing
            } else if (messageBox.getText().equals(".clear")) {
                chatBox.setText("Cleared all messages\n");
                messageBox.setText("");
            } else {
            //    chatBox.append("<" + username + ">:  " + messageBox.getText() + "\n");
            test.send(messageBox.getText());
            messageBox.setText("");
            }
            messageBox.requestFocusInWindow();
        }
    }

    
    class enterServerButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            
            username = usernameChooser.getText();
            ip  = server.getText();
            portNum  = Integer.parseInt(port.getText());
            try {
                sock = new Socket(ip, portNum);
                test = new ChatClient(sock, username);
                Thread T = new Thread(test);
                T.start();
                preFrame.setVisible(false);
                display();
            } catch (Exception e) {
                System.out.println(e);
            }
            
            test.connect();
            
            /*if (username.length() < 1) {
                System.out.println("No!");
            } else {
                
                preFrame.setVisible(false);
                display();
            }*/
        }
              
    }
  
   
    
}