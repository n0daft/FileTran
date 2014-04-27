package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Eric on 27.04.2014.
 */
public class Server extends Thread{

    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;

   // private String filePath = "C:/test/blub.pdf";

    public Server(){
        try {
            serverSocket = new ServerSocket(8989);

            if (!serverSocket.isBound())
                System.out.println("Sever Socket not Bounded...");
            else
                System.out.println("Server Socket bounded to Port : " + serverSocket.getLocalPort());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){

        while (true){
            try {
                // Wait for client and connect on request.
                clientSocket = serverSocket.accept();
                if (!clientSocket.isConnected())
                    System.out.println("Client Socket not Connected...");
                else
                    System.out.println("Client Socket Connected : " + clientSocket.getInetAddress());


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean transfer(String filePath){
        boolean success = false;

        /* -- Read bytes from disk -- */
        // Prepare InputStream
        File fileToSend = new File(filePath);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileToSend);

            BufferedInputStream bis = new BufferedInputStream(fis);

            // Reading the specified file into buffer.
            byte[] mybytearray = new byte[(int) fileToSend.length()];
            bis.read(mybytearray, 0, mybytearray.length);
            bis.close();

            /* -- Send bytes over connection -- */
            // Prepare OutputStream
            OutputStream out = clientSocket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);

            // Send filename and size first, then the files content.
            dos.writeUTF(fileToSend.getName());
            dos.writeLong(mybytearray.length);
            dos.write(mybytearray, 0, mybytearray.length);
            dos.flush();
            dos.close();

            clientSocket.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return success;

    }

    public boolean isConnected() {
        boolean connected = false;
        if (clientSocket != null) {
            connected = clientSocket.isConnected();
        }
        return connected;
    }

}
