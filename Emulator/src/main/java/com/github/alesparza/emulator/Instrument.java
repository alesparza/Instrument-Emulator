package com.github.alesparza.emulator;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Represents an Instrument.
 */
public class Instrument {

    private OutputStream out;

    private InputStream in;

    /**
     * Initialises this instrument as a client configuration.
     * @param hostname hostname of the server to connect to
     * @param port port number the server is listening on
     */
    public void initialiseSocket(String hostname, int port) {
        try {
            Socket socket = new Socket(hostname, port);
            out = socket.getOutputStream();
            in = socket.getInputStream();
        } catch (IOException e) {
            System.out.println("Error creating client socket: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Initialises this instrument as a server configuration.
     * @param port the port number to listen on
     */
    public void initialiseSocket(int port) {
        System.err.println("Error: cannot use instrument as server");
        throw new RuntimeException("Error: cannot use instrument as server");
    }

    /**
     * Send a message from this instrument.
     * @param message the message to send
     */
    public void sendMessage(String message) {
        sendMessage(message.getBytes());
    }

    /**
     * Send a message from this instrument.
     * @param message the message to send
     */
    public void sendMessage(byte[] message) {
        try {
            out.write(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get message to this instrument.
     * @return the message sent to this instrument
     */
    public byte[] receiveMessage() {
        byte[] message = new byte[1024];
        byte[] ret;
        try {
            ret = new byte[in.read(message)] ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

}
