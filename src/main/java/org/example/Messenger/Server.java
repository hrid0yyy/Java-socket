package org.example.Messenger;
import org.example.chatSession;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket serverSocket =  new ServerSocket(12345);
        ObjectOutputStream oos;
        ObjectInputStream ois;
        while (true){
            Socket socket = serverSocket.accept();
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            String name = (String) ois.readObject();
            System.out.println(name+" Connected");
            chatSession.addClient(name,oos);
            new Handler(ois,name);

        }
    }
}
class Handler implements Runnable{
    ObjectInputStream ois;
    String name;
    Handler(ObjectInputStream ois,String name){
        this.ois = ois;
        this.name = name;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true){
            try {
                String obj = (String) ois.readObject();
                String[] info = obj.split("\\$");
                //info[0] = sender, info[1] = receiver, info[2] = message
                if(info[1].equals("list")){
                    String list = chatSession.list();
                    ObjectOutputStream stream = chatSession.getStream(name);
                    stream.writeObject(list);
                }
                else {
                    String msg = info[0] + " : " + info[2];
                    ObjectOutputStream stream = chatSession.getStream(info[1]);
                    stream.writeObject(msg);
                }

            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
