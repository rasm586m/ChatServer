import java.io.*;
import java.util.*;
import java.net.*;

public class Server {

    static Vector<ClientHandler> clientList = new Vector<>();
    static ArrayList<String> clientUsernames = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(5050);

        while (true) {

            System.out.println("Waiting for client to connect...");

            Socket s = ss.accept();

            System.out.println("New client request received : " + s);

            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            System.out.println("Creating a new handler for this client...");

            ClientHandler clientHandler = new ClientHandler(s, dis, dos);

            Thread t = new Thread(clientHandler);

            System.out.println("Adding this client to active client list");

            clientList.add(clientHandler);

            t.start();
        }
    }

}

