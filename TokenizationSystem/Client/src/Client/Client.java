package Client;
import com.sun.source.tree.WhileLoopTree;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client{
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;

    public Client(String address, int port){
        try {
            socket = new Socket(address, port);
            System.out.println("connected");

            input = new DataInputStream(socket.getInputStream());

            out = new DataOutputStream(socket.getOutputStream());

        }catch (UnknownHostException e){
            e.printStackTrace();
        }catch (IOException i){
            i.printStackTrace();
        }


    }


    public void sendMessage(String message) {
        try {
            out.writeUTF(message);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public String readMessage(){

        try {
            return input.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }


}
