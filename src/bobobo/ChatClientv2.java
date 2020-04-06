/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bobobo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JButton;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * A simple Swing-based client for the chat server.  Graphically
 * it is a frame with a text field for entering messages and a
 * textarea to see the whole dialog.
 *
 * The client follows the Chat Protocol which is as follows.
 * When the server sends "SUBMITNAME" the client replies with the
 * desired screen name.  The server will keep sending "SUBMITNAME"
 * requests as long as the client submits screen names that are
 * already in use.  When the server sends a line beginning
 * with "NAMEACCEPTED" the client is now allowed to start
 * sending the server arbitrary strings to be broadcast to all
 * chatters connected to the server.  When the server sends a
 * line beginning with "MESSAGE " then all characters following
 * this string should be displayed in its message area.
 */
public class ChatClientv2 {

    BufferedReader in;
    PrintWriter out;
    game nonon = new game();
    JTextField textField = nonon.write;
    JTextArea messageArea = nonon.display;
    JButton joinGame = nonon.joinbut;
    boolean joinDaraagui=true;
    JButton readybut = nonon.readybut;
    boolean readyDaraagui=true;
    
    /**
     * Constructs the client by laying out the GUI and registering a
     * listener with the textfield so that pressing Return in the
     * listener sends the textfield contents to the server.  Note
     * however that the textfield is initially NOT editable, and
     * only becomes editable AFTER the client receives the NAMEACCEPTED
     * message from the server.
     */
    public ChatClientv2() {

        // Layout GUI
        textField.setEditable(false);
        messageArea.setEditable(false);

        // Add Listeners
        textField.addActionListener(new ActionListener() {
            /**
             * Responds to pressing the enter key in the textfield by sending
             * the contents of the text field to the server.    Then clear
             * the text area in preparation for the next message.
             */
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });
        
    }
    //servert holbo !!!!!!
    /**
     * Prompt for and return the desired screen name.
     *//* ner ug!!!!!!!!!!!!!!!!!!!
    */private String getName() {
        return JOptionPane.showInputDialog(
            nonon.contentPanel,
            "Choose a screen name:",
            "Screen name selection",
            JOptionPane.PLAIN_MESSAGE);
    }
    

    /**
     * Connects to the server then enters the processing loop.
     */
    private void run() throws IOException {
        String hostAddress = "localhost";
        // Make connection and initialize streams
        String serverAddress = hostAddress;
        Socket socket = new Socket(serverAddress, 9001);
        in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        
        joinGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(joinDaraagui){
                    out.println("toglogch "+nonon.myname);
                    joinDaraagui=false;
                }
            }
        });
        readybut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(readyDaraagui){
                    out.println("ready");
                    readyDaraagui=false;
                }
            }
        });
        // Process all messages from server, according to the protocol.
        while (true) {
            String line = in.readLine();
            if (line.startsWith("SUBMITNAME")) {
                String temp=getName();
                nonon.myname=temp;
                System.out.println("utga yawuullaa");
                out.println(temp);
            } else if (line.startsWith("NAMEACCEPTED")) {
                textField.setEditable(true);
            } else if (line.startsWith("MESSAGE")) {
                messageArea.append(line.substring(8) + "\n");
            } else if (line.startsWith("newplayer")){
                nonon.playerEntered(line.substring(10));
            } else if (line.startsWith("ehel")){
                nonon.gameStarted();
            } else if (line.startsWith("tsagdaa")){
                System.out.println(line);
                nonon.checkPolice(line.substring(8));
            } else if (line.startsWith("mafia")){
                System.out.println(line);
                nonon.checkMafia(line.substring(6));
            } else if (line.startsWith("emch")){
                System.out.println(line);
                nonon.checkDoctor(line.substring(5));
            }
        }
    }

    /**
     * Runs the client as an application with a closeable frame.
     */
    public static void main(String[] args) throws Exception {
        ChatClientv2 client = new ChatClientv2();
        
        client.nonon.setVisible(true);
        client.run();
    }
}