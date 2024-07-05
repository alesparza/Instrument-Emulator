package com.github.alesparza.emulator.instrument;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Represents an Instrument Connection.
 */
public class InstrumentConnection {

  private OutputStream out;

  private InputStream in;

  private Socket socket;

  private ServerSocket serverSocket;

  private boolean isInit = false;


  /**
   * Initialises this instrument as a client configuration.
   * @param hostname hostname of the server to connect to
   * @param port port number the server is listening on
   */
  public String initialise(String hostname, int port) {
    if (isInit) return "Error: already started";
    try {
      Socket socket = new Socket(hostname, port);
      out = socket.getOutputStream();
      in = socket.getInputStream();
    } catch (IOException e) {
      return "Error creating client socket: " + e.getMessage();
    }
    isInit = true;
    return "Connected to " + hostname + ":" + port;
  }

  /**
   * Initialises this instrument as a server configuration.
   * @param port the port number to listen on
   */
  public String initialise(int port) {
    if (isInit) return "Error: already started";
    return "Error: cannot use instrument as server (not implemented)";
  }

  /**
   * Send a message from this instrument.
   * @param message the message to send
   */
  public String sendMessage(byte[] message) {
    try {
      out.write(message);
      out.flush();
    } catch (IOException e) {
      return "Error sending message: " + e.getMessage();
    }
    return "";
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
   * Shuts down the instrument and closes all connections.
   */
  public String shutdown() {
    if (!isInit) return "Error: not started";

    if (this.socket != null) {
      try {
        this.socket.close();
      } catch (IOException e) {
        return "Error closing socket: " + e.getMessage();
      }
    }
    if (this.serverSocket != null) {
      try {
        this.serverSocket.close();
      } catch (IOException e) {
        return "Error closing server socket: " + e.getMessage();
      }
    }
    if (this.in != null) {
      try {
        in.close();
      } catch (IOException e) {
        return "Error closing input stream: " + e.getMessage();
      }
    }
    if (this.out != null) {
      try {
        out.close();
      } catch (IOException e) {
        return "Error closing output stream: " + e.getMessage();
      }
    }
    serverSocket = null;
    socket = null;
    in = null;
    out = null;
    isInit = false;
    return "Shut down successful.";
  }

}
