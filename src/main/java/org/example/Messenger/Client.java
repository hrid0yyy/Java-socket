package org.example.Messenger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",12345);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Scanner s = new Scanner(System.in);
        System.out.println("Enter Your name first");
        String name = s.nextLine();
        oos.writeObject(name);
        System.out.println("Type list for active list");
        System.out.println("Receiver name + $ + your message");
        Thread clientWriter = new Thread(()->{
            Scanner sc = new Scanner(System.in);
            String msg;
            while (true){
                msg = sc.nextLine();
                try {
                    String finalMessage = name+"$"+msg;
                    oos.writeObject(finalMessage);
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
