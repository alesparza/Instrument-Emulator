package com.github.alesparza.emulator.instrument;

import com.github.alesparza.ascii.Ascii;
import com.github.alesparza.ascii.Ascii.CntlChar;
import com.github.alesparza.astm.component.Record;
import com.github.alesparza.astm.protcol.AstmConfiguration;
import com.github.alesparza.astm.protcol.AstmProtocol;

import javax.swing.*;
import java.util.Arrays;

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
   * The ASTM configuration this instrument uses to communicate.
   */
  private final AstmConfiguration asmtConfiguration;

  /**
   * Constructs a new instrument with the default ASTM configuration.
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
    // setup ASTM configuration based on the type
    switch (type) {
      case GENERIC:
        asmtConfiguration = new AstmConfiguration();
        break;
      // based on DxH ASTM protocol publically available from Beckman Coulter's website.
      case DxH:
        asmtConfiguration = new AstmConfiguration('|', '\\', '!', '~',64000);
        break;
      default:
        asmtConfiguration = new AstmConfiguration();
    }


  }

  /**
   * Constructs a new instrument.
   * @param name name of instrument
   * @param type type of instrument
   * @param hostname hostname for communication
   * @param port port for communication
   * @param astmConfiguration astm configuration
   */
  public Instrument(String name, InstrumentType type, String hostname, int port, AstmConfiguration astmConfiguration) {
    this.type = type;
    this.name = name;
    this.hostname = hostname;
    this.port = port;
    connection = new InstrumentConnection(hostname, port);
    this.asmtConfiguration = astmConfiguration;
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
   * Prints a message to the console log area.
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
   * Listens for incoming connections
   */
  public void listen() {
    printConsoleLn(connection.serverListen());
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
    sendENQ();
    boolean check = checkForACK();
    sendEOT();
    if (!check) {
      printConsoleLn("did not receive ACK from LIS");
    }
    else {
      printConsoleLn("Connection check successful");
    }
  }

  /**
   * Sends ENQ to start an ASTM message.
   *
   * @return
   */
  public boolean sendENQ() {
    if (!connection.isInit()) {
      printConsoleLn("Error: not started");
      return false;
    }
    String ret;
    byte[] message = new byte[] {CntlChar.ENQ.getAsciiByte()};
    ret = connection.sendMessage(message);
    if (!ret.isEmpty()) {
      printConsoleLn(ret);
      return false;
    }
    printCommLn("--> " + Ascii.getFormattedString(message));
    return true;
  }

  /**
   * Sends EOT to end an ASTM message.
   * @return
   */
  public boolean sendEOT() {
    if (!connection.isInit()) {
      printConsoleLn("Error: not started");
      return false;
    }
    String ret;
    byte[] message = new byte[] {CntlChar.EOT.getAsciiByte()};
    ret = connection.sendMessage(message);
    if (!ret.isEmpty()) {
      printConsoleLn(ret);
      return false;
    }
    printCommLn("--> " + Ascii.getFormattedString(message));
    return true;
  }

  /**
   * checks if response is ACK
   * @return <code>true</code> if the response is ACK, <code>false</code>otherwise.
   */
  public boolean checkForACK() {
    byte[] receive = connection.receiveMessage();
    printCommLn("<-- " + Ascii.getFormattedString(receive));
    if (receive[0] == CntlChar.ACK.getAsciiByte()) {
      return true;
    };
    return false;
  }

  /**
   * Resets the connection.
   * <br>
   * Shutdown the connection and re-initialise.
   */
  public void reset() {
    disconnect();
    connect();
  }

  /**
   * Sends the H record(s).
   * @return <code>true</code> if entire message sent and ACK'd successfully, <code>false</code>otherwise.
   */
  public int sendHRecord(int startFrame, Record record) {
    int frame = startFrame;
    byte[] data = AstmProtocol.generateHRecord(asmtConfiguration, record);

    // TODO: loop through astmConfiguration.getDataLength() characters, incrementing frame number
    byte[] toSend = AstmProtocol.generateMessage(frame, data, true);
    connection.sendMessage(toSend);
    printCommLn("--> " + Ascii.getFormattedString(toSend));
    if (!checkForACK()) {
      printConsoleLn("Failed to send message");
      return AstmProtocol.INVALID_FRAME;
    }
    return frame;
  }

  /**
   * Sends the P record(s).
   * @return <code>true</code> if entire message sent and ACK'd successfully, <code>false</code>otherwise.
   */
  public int sendPRecord(int startFrame, Record record) {
    int frame = startFrame;
    byte[] data = AstmProtocol.generatePRecord(asmtConfiguration, record);

    // TODO: loop through astmConfiguration.getDataLength() characters, incrementing frame number
    byte[] toSend = AstmProtocol.generateMessage(frame, data, true);
    connection.sendMessage(toSend);
    printCommLn("--> " + Ascii.getFormattedString(toSend));
    if (!checkForACK()) {
      printConsoleLn("Failed to send message");
      return AstmProtocol.INVALID_FRAME;
    }
    return frame;
  }

  /**
   * Sends the O record(s).
   * @return <code>true</code> if entire message sent and ACK'd successfully, <code>false</code>otherwise.
   */
  public int sendORecord(int startFrame, Record record) {
    int frame = startFrame;
    byte[] data = AstmProtocol.generateORecord(asmtConfiguration, record);

    // TODO: loop through astmConfiguration.getDataLength() characters, incrementing frame number
    byte[] toSend = AstmProtocol.generateMessage(frame, data, true);
    connection.sendMessage(toSend);
    printCommLn("--> " + Ascii.getFormattedString(toSend));
    if (!checkForACK()) {
      printConsoleLn("Failed to send message");
      return AstmProtocol.INVALID_FRAME;
    }
    return frame;
  }

  /**
   * Sends the R record(s).
   * @return <code>true</code> if entire message sent and ACK'd successfully, <code>false</code>otherwise.
   */
  public int sendRRecord(int startFrame, int startSequence, Record record) {
    int frame = startFrame;
    int sequence = startSequence;
    byte[] data = AstmProtocol.generateRRecord(asmtConfiguration, sequence, record);

    // TODO: loop through astmConfiguration.getDataLength() characters, incrementing frame number
    byte[] toSend = AstmProtocol.generateMessage(frame, data, true);
    connection.sendMessage(toSend);
    printCommLn("--> " + Ascii.getFormattedString(toSend));
    if (!checkForACK()) {
      printConsoleLn("Failed to send message");
      return AstmProtocol.INVALID_FRAME;
    }
    return sequence;
  }

  /**
   * Sends the L record(s).
   * @return <code>true</code> if entire message sent and ACK'd successfully, <code>false</code>otherwise.
   */
  public int sendLRecord(int startFrame, Record record) {
    int frame = startFrame;
    byte[] data = AstmProtocol.generateLRecord(asmtConfiguration, record);

    byte[] toSend = AstmProtocol.generateMessage(frame, data, true);
    connection.sendMessage(toSend);
    printCommLn("--> " + Ascii.getFormattedString(toSend));
    if (!checkForACK()) {
      printConsoleLn("Failed to send message");
      return AstmProtocol.INVALID_FRAME;
    }
    return frame;
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
