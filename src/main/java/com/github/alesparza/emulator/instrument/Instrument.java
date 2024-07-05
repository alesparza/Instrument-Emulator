package com.github.alesparza.emulator.instrument;

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

  /**
   * Connect the instrument.
   * @return success or failure message.
   */
  public String connect() {
    if (hostname == null || hostname.isEmpty()) {
      return connection.initialise(port);
    }
    else {
      return connection.initialise(hostname, port);
    }
  }

  /**
   * Disconnect the instrument.
   * @return success or failure message.
   */
  public String disconnect() {
    return connection.shutdown();
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
