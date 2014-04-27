package sample;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Eric on 27.04.2014.
 */
public class Client {

    public static void main(String[] args){
        String address = "localhost";
        String destination = "C:/test/em/";
        int port = 8989;

        Socket socket;

        try {
            socket = new Socket(address, port);

            if (!socket.isConnected())
                System.out.println("Socket Connection Not established");
            else
                System.out.println("Socket Connection established : " + socket.getInetAddress());

			/* -- Receive bytes over connection -- */
            // Prepare InputStream
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            // Receive file information
            String fileName = dis.readUTF();
            long size = dis.readLong();

			/* -- Write bytes to disk -- */
            // Prepare file
            OutputStream fos = new FileOutputStream(destination + fileName);

            System.out.println("Writing bytes to disk... Filename: " + fileName + ", Filesize: " + size);

            byte[] buffer = new byte[1024];
            int bytesRead;

            // Always read as many bytes as the buffer size until the buffer is greater than the remaining
            // bytes of the file. If so, read just the remaining bytes.
            while (size > 0 && (bytesRead = is.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                fos.write(buffer, 0, bytesRead);
                size -= bytesRead;
            }

            fos.close();
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
