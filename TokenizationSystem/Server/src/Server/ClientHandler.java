package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {

    final DataInputStream in;
    final DataOutputStream out;
    final Socket socket;

    public ClientHandler(DataInputStream in, DataOutputStream out, Socket socket) {
        this.in = in;
        this.out = out;
        this.socket = socket;
    }

    @Override
    public void run() {
        String received;
        String toReturn;

        while(true){

            try {
                received = in.readUTF();
                System.out.println(received);

                switch (received){
                    case "hello" -> toReturn = "hi";
                    case "Ivan" -> toReturn = "pedal";
                    case "Pavel" -> toReturn = "gotin";
                    default -> toReturn = "";
                }
                out.writeUTF(toReturn);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
