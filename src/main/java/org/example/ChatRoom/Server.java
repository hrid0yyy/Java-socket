package org.example.ChatRoom;

import org.example.chatSession;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Objects;


public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket =  new ServerSocket(8080);
        ObjectOutputStream oos;
        int serial = 0;
        while (true){
            Socket socket = serverSocket.accept();
            String clientName = "Client-"+(serial++);
            System.out.println(clientName+" Connected");
            oos = new ObjectOutputStream(socket.getOutputStream());
            chatSession.addClient(clientName,oos);
            new ServerHelper(socket,clientName);
        }
    }
}
class ServerHelper implements Runnable{

    ObjectInputStream ois;
    String name;


    ServerHelper(Socket socket,String name) throws IOException {
        ois = new ObjectInputStream(socket.getInputStream());
        this.name = name;
        new Thread(this).start();
    }


    @Override
    public void run() {
        while (true){
            try {
                Object obj = ois.readObject();
                String msg = name + " : " +  obj;
                chatSession.saveMessageToFile(msg);
                chatSession.broadCast(msg,name);

            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }
}