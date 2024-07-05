package com.github.alesparza.emulator.instrument;

import javax.swing.*;

/**
 * Represents a single Instrument.
 */
public class Instrument {

  /**
   * The name of this instrument.
   */
  private final String name;

  /**
   * The InstrumentType of this instrument.
   */
  private final InstrumentType type;

  /**
   * Hostname this instrument connects to.  Empty if acting as server.
   */
  private final String hostname;

  /**
   * Port number this instrument communicates on.
   */
  private final int port;

  private JTextArea consoleTextArea;

  private JTextArea commTextArea;


  /**
   * The Instrument Connection this instrument uses to communicate.
   */
  private final InstrumentConnection connection;

  public Instrument(String name, InstrumentType type, String hostname, int port) {
    this.type = type;
    this.name = name;
    this.hostname = hostname;
    this.port = port;
    connection = new InstrumentConnection();
  }

  public void setGUIComponents(JTextArea consoleTextArea, JTextArea commTextArea) {
    this.consoleTextArea = consoleTextArea;
    this.commTextArea = commTextArea;
  }

  public void printConsoleLn(String message) {
    this.consoleTextArea.append(message + "\n");
    this.consoleTextArea.setCaretPosition(this.consoleTextArea.getDocument().getLength());
  }

  public void printCommLn(String message) {
    this.commTextArea.append(message + "\n");
    this.commTextArea.setCaretPosition(this.commTextArea.getDocument().getLength());
  }


  /**
   * Connect the instrument.
   */
  public void connect() {
    if (hostname == null || hostname.isEmpty()) {
      printConsoleLn(connection.initialise(port));
    }
    else {
      printConsoleLn(connection.initialise(hostname, port));
    }
  }

  /**
   * Disconnect the instrument.
   */
  public void disconnect() {
    printConsoleLn(connection.shutdown());
  }

  /**
   * Check if the connection is okay.
   * <br>
   * Sends ENQ, expects ACK, sends EOT.
   */
  public void check() {
    byte[] message = new byte[] {0x5};
    connection.sendMessage(message);
    printCommLn("--> " + new String(message));
    byte[] receive = connection.receiveMessage();
    printCommLn("<-- " + new String(receive));
    message = new byte[] {0x4};
    connection.sendMessage(message);
    printCommLn("--> " + new String(receive));
  }

  public void reset() {
    connection.shutdown();
    connection.initialise();
  }


  /**
   * Gets the name of the instrument.
   * @return the name of the instrument
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the type of the instrument.
   * @return the InstrumentType of the instrument
   */
  public InstrumentType getType() {
    return type;
  }

  public String getHostname() {
    return hostname;
  }

  public int getPort() {
    return port;
  }

}
