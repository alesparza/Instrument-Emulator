package com.github.alesparza.emulator;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class Emulator {
    public static void main(String[] args) {

        // set up the emulator
        if (args.length != 2) {
            System.out.println("Usage: java Emulator hostname port");
        }
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        // send an ENQ, wait for ACK, close with EOT
        try {
            // configure the client
            Socket clientSocket;

            clientSocket = new Socket(hostName, portNumber);
            OutputStream out = clientSocket.getOutputStream();

            byte[] message = {(byte) 5}; // ENQ
            byte[] response = new byte[] {0};
            out.write(message);
            InputStream in = clientSocket.getInputStream();
            in.read(response); // expecting ACK (0x6)

            System.out.println(new String(response, StandardCharsets.US_ASCII));
            message = new byte[]{(byte) 4}; // EOT
            out.write(message);

        } catch (UnknownHostException e) {
            System.err.println("Unknown host");
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("IOException");
            throw new RuntimeException(e);
        }

        System.out.println("Bye bye!");
    }
}