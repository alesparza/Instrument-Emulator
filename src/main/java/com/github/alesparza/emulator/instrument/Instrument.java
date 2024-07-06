package com.github.alesparza.emulator.instrument;

import com.github.alesparza.astm.Ascii;
import com.github.alesparza.astm.Ascii.CntlChar;

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

  /**
   * Text area for console logging.
   */
  private JTextArea consoleTextArea;

  /**
   * Text area for communication logging.
   */
  private JTextArea commTextArea;


  /**
   * The Instrument Connection this instrument uses to communicate.
   */
  private final InstrumentConnection connection;

  /**
   * Constructs a new instrument.
   * @param name name of instrument
   * @param type type of instrument
   * @param hostname hostname for communication
   * @param port port for communication
   */
  public Instrument(String name, InstrumentType type, String hostname, int port) {
    this.type = type;
    this.name = name;
    this.hostname = hostname;
    this.port = port;
    connection = new InstrumentConnection(hostname, port);
  }

  /**
   * Sets the text areas to print to for logging.
   * @param consoleTextArea console log area
   * @param commTextArea communication log area
   */
  public void setGUIComponents(JTextArea consoleTextArea, JTextArea commTextArea) {
    this.consoleTextArea = consoleTextArea;
    this.commTextArea = commTextArea;
  }

  /**
   * Prints a messae to the console log area.
   * @param message the message to print
   */
  public void printConsoleLn(String message) {
    this.consoleTextArea.append(message + "\n");
    this.consoleTextArea.setCaretPosition(this.consoleTextArea.getDocument().getLength());
  }

  /**
   * Prints a message to the communication log area.
   * @param message the message to print
   */
  public void printCommLn(String message) {
    this.commTextArea.append(message + "\n");
    this.commTextArea.setCaretPosition(this.commTextArea.getDocument().getLength());
  }


  /**
   * Connect the instrument.
   */
  public void connect() {
    printConsoleLn(connection.initialise());
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
    byte[] message = new byte[] {CntlChar.ENQ.getAsciiByte()};
    connection.sendMessage(message);
    printCommLn("--> " + Ascii.getFormattedString(message));
    byte[] receive = connection.receiveMessage();
    printCommLn("<-- " + Ascii.getFormattedString(receive));
    message = new byte[] {CntlChar.EOT.getAsciiByte()};
    connection.sendMessage(message);
    printCommLn("--> " + Ascii.getFormattedString(message));
  }

  /**
   * Resets the connection.
   * <br>
   * Shutdown the connection and re-initialise.
   */
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

  /**
   * Gets the hostname of the instrument.
   * @return the hostname of the instrument
   */
  public String getHostname() {
    return hostname;
  }

  /**
   * Gets the port of the instrument.
   * @return the port of the instrument
   */
  public int getPort() {
    return port;
  }

}
