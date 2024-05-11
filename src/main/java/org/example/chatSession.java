package org.example;

import java.io.*;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class chatSession {
    private static final HashMap<String, ObjectOutputStream> clients = new HashMap<>();
    public static void addClient(String name, ObjectOutputStream oos) { clients.put(name,oos); }
    public static HashMap<String, ObjectOutputStream> getClients() { return clients;}
    public static void saveMessageToFile(String message) throws IOException {
        try {
            FileWriter writer = new FileWriter("message_history.txt", true); // Append mode
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(message + "\n");
            bufferedWriter.close();
        } catch (IOException e) {
            throw new IOException();
        }
    }

    public static ObjectOutputStream getStream(String name){
        return clients.get(name);
    }

    public static void previousTexts() throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("message_history.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        } catch (IOException e) {
            throw new IOException();
        }
    }

    public static String list() {
        StringBuilder list = new StringBuilder();
        for (HashMap.Entry<String, ObjectOutputStream> entry : chatSession.getClients().entrySet()) {
            String clientName = entry.getKey();
            list.append(clientName).append("\n");
        }
        return list.toString();
    }
}
