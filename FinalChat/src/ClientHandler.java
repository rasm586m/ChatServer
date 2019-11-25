import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler implements Runnable {

    private final DataInputStream dis;
    private final DataOutputStream dos;
    private Socket s;
    private String username;
    private ServerHandler serverHandler = new ServerHandler();

    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.s = s;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String joinRequest = dis.readUTF();
                username = joinRequest.substring(5, joinRequest.indexOf(','));
                String verifyName = serverHandler.createName(username);
                dos.writeUTF(verifyName);
                if (verifyName.equals("J_OK"))
                    break;
            }
            broadCast();

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            s.setSoTimeout(90000);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                String msg = dis.readUTF();

                if (msg.equals("IMAV")){
                    System.out.println("Server recieved heartbeat from: " + username);
                }
                else {

                    for (ClientHandler allClients : Server.clientList) {
                        allClients.dos.writeUTF(username + " : " + msg);
                    }
                    if (msg.equals("Quit")) {
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println(username + ": has lost connetion to the server");
                break;
            }
        }
        Server.clientList.remove(this);
        Server.clientUsernames.remove(username);

        try {
            s.close();
            dis.close();
            dos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadCast() throws IOException {
        for (ClientHandler client : Server.clientList) {
            client.dos.writeUTF("[ Users connected to the server ]");
            for (String clients : Server.clientUsernames) {
                client.dos.writeUTF(">> " + clients + " <<");
            }
        }
    }

}
