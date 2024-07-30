package com.github.alesparza.emulator.instrument;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

/**
 * Represents an Instrument Connection.
 */
public class InstrumentConnection {

  /**
   * Hostname for communication.
   */
  private final String hostname;

  /**
   * Port number for communication.
   */
  private final int port;

  /**
   * Communication output stream.
   */
  private OutputStream out;

  /**
   * Communication input stream.
   */
  private InputStream in;

  /**
   * Socket for communication.
   */
  private Socket socket;

  /**
   * Socket for server
   */
  private ServerSocket serverSocket;

  /**
   * Flag for if the connection is initialised or not.
   */
  private boolean isInit = false;

  /**
   * Flag for if the connection is acting as a server or client.
   */
  private boolean isServer = false;

  /**
   * Flag for if the connection is established or not.
   */
  private boolean isConnected = false;

  /**
   * Constructs a new connection for an instrument.
   * @param hostname hostname for communication
   * @param port port for communication
   */
  public InstrumentConnection(String hostname, int port) {
    this.hostname = hostname;
    this.port = port;
    if (this.hostname.isEmpty()) isServer = true;
  }

  /**
   * Initialise the instrument connection.
   * @return status message
   */
  public String initialise() {
    if (this.hostname.isEmpty()) {
      return initialise(port);
    }
    return initialise(hostname, port);
  }

  /**
   * Initialises this instrument as a client configuration.
   * @param hostname hostname of the server to connect to
   * @param port port number the server is listening on
   * @return status message
   */
  public String initialise(String hostname, int port) {
    if (isInit) return "Error: already started";
    int timeout = 1000;
    try {
      socket = new Socket(hostname, port);
      socket.setSoTimeout(timeout);
      out = socket.getOutputStream();
      in = socket.getInputStream();
      isConnected = true;
      isInit = true;
      return "Connected to " + hostname + ":" + port;
    } catch (IOException e) {
      return "Error creating client socket: " + e.getMessage();
    }
  }

  /**
   * Initialises this instrument as a server configuration.
   * @param port the port number to listen on
   * @return status message
   */
  public String initialise(int port) {
    if (isInit) return "Error: already started";
    int timeout = 1000;
    try {
      serverSocket = new ServerSocket(port);
      isInit = true;
      return "Started server on port " + port;
    } catch (IOException e) {
      isInit = false;
      return "Error starting server: " + e;
    }
  }

  /**
   * If not connected, tries to listen for incoming connections and accept them.
   * @return
   */
  public String serverListen() {
    if (!isInit) return "Error: cannot listen for incoming connections when not started";
    if (isConnected) return "Already connected to client";
    int timeout = 5000;
    try {
      serverSocket.setSoTimeout(timeout);
      socket = serverSocket.accept();
      socket.setSoTimeout(timeout);
    } catch (SocketException e) {
      return "Error setting up socket timeout";
    } catch (SocketTimeoutException e) {
      return "Timed out after " + timeout / 1000 + " seconds waiting for connection to port " + port + ".  Try again later.";
    } catch (IOException e) {
      return "Error accepting incoming connection: " + e;
    }
    isConnected = true;
    return "Accepted connection from " + socket.getInetAddress() + ":" + socket.getPort();
  }

  /**
   * Send a message from this instrument.
   * @param message the message to send
   */
  public String sendMessage(byte[] message) {
    if (!isInit) {
      return "Error: not started";
    }
    if (isServer && !isConnected) {
      return "Error: not connected to client";
    }
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
    if (!isInit) throw new IllegalStateException("Not started!");
    // TODO: have this throw a ConnectionException and catch later
    if (isServer && !isConnected) {
      throw new IllegalStateException("Not connected to client");
    }
    byte[] message = new byte[1024];
    byte[] ret;
    try {
      int read = in.read(message);
      ret = Arrays.copyOfRange(message,0,read);
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

  /**
   * Checks if socket is initialised.
   * @return true if initialised, otherwise false
   */
  public boolean isInit() {
    return this.isInit;
  }

  /**
   * Checks if socket is server.
   * @return true if is a server, otherwise false
   */
  public boolean isServer() {
    return this.isServer;
  }

  /**
   * Checks if socket  is connected.
   * @return true if connected, otherwise false
   */
  public boolean isConnected() {
    return this.isConnected;
  }

}
