package com.github.alesparza.emulator.processor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Represents an Instrument.
 */
public class Instrument {

  private OutputStream out;

  private InputStream in;

  private Socket socket;

  private ServerSocket serverSocket;

  /**
   * Initialises this instrument as a client configuration.
   * @param hostname hostname of the server to connect to
   * @param port port number the server is listening on
   */
  public void initialiseSocket(String hostname, int port) {
    this.shutdown();
    try {
      Socket socket = new Socket(hostname, port);
      out = socket.getOutputStream();
      in = socket.getInputStream();
    } catch (IOException e) {
      System.err.println("Error creating client socket: " + e.getMessage());
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

  /**
   * Sends ENQ (0x5) from this instrument.
   */
  public void sendEnq() {
    sendMessage(new byte[] {0x5});
  }

  /**
   * Sends ACK (0x4) from this instrument.
   */
  public void sendAck() {
    sendMessage(new byte[] {0x4});
  }

  /**
   * Shuts down the instrument and closes all connections.
   */
  public void shutdown() {
    if (this.socket != null) {
      try {
        this.socket.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    if (this.serverSocket != null) {
      try {
        this.serverSocket.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    if (this.in != null) {
      try {
        in.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    if (this.out != null) {
      try {
        out.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    serverSocket = null;
    socket = null;
    in = null;
    out = null;
  }

}
