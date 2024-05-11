package org.example.ChatRoom;

import org.example.chatSession;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",8080);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        chatSession.previousTexts();
        Thread clientWriter = new Thread(()->{
            Scanner sc = new Scanner(System.in);
            String msg;
           while (true){
               msg = sc.nextLine();
               try {
                   oos.writeObject(msg);
               } catch (IOException e) {
                   throw new RuntimeException(e);
               }
           }
        });
        clientWriter.start();
        Thread clientReader = new Thread(()->{
            Object msg;
            while (true){
                try {
                    msg = ois.readObject();
                    System.out.println((String) msg);
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        clientReader.start();
    }
}
