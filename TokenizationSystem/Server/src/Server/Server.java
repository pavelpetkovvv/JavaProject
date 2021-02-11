package Server;
import Accounts.Application;
import Accounts.BankClient;

import java.net.*;
import java.io.*;

public class Server {

    public static void main(String[] args)throws IOException {

        Application database = new Application();

        ServerSocket serverSocket = new ServerSocket(8080);
        while(true){
            Socket socket = null;

            try {
                socket = serverSocket.accept();

                System.out.println("A new client is connected : " + socket);

                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                System.out.println("Assigning new thread for this client");

                Thread t = new ClientHandler(in, out, socket);

                t.start();
            }catch (Exception e){
                socket.close();
                e.printStackTrace();
            }
        }
    }
}
