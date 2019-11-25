import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    private final static Scanner scn = new Scanner(System.in);

    public static void main(String args[]) throws UnknownHostException, IOException {

        DataInputStream dis;
        DataOutputStream dos;

        while (true) {
            System.out.println("Enter username");
            String username = scn.nextLine();
            System.out.println("Enter server IP");
            String ipString = scn.nextLine();
            System.out.println("Enter Port");
            int port = scn.nextInt();
            scn.nextLine();
            try {
                InetAddress ip = InetAddress.getByName(ipString);
                Socket s = new Socket(ip, port);
                dis = new DataInputStream(s.getInputStream());
                dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF("JOIN " + username + ", " + ipString + ":" + port);
                String result = dis.readUTF();
                System.out.println(result);
                if (result.equals("J_OK")) {
                    break;
                }
            } catch (IOException e) {
                System.out.println("IP or PORT is wrong. Try again...");
            }
        }

        DataOutputStream finalDos = dos;
        Thread sendMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String msg = scn.nextLine();
                    try {
                        finalDos.writeUTF(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        });

        DataInputStream finalDis = dis;
        Thread readMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String msg = finalDis.readUTF();
                        System.out.println(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        });

        DataOutputStream finalDos1 = dos;
        Thread heartBeat = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(60000);
                        finalDos1.writeUTF("IMAV");
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        sendMessage.start();
        readMessage.start();
        heartBeat.start();
    }

}


