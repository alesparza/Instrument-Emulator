package com.github.alesparza.astm.component;

/**
 * Represents a single frame.
 * <br>
 * This includes the starting STX and ends with LF.
 */
public class Frame {
  /**
   * The frame number of the frame.
   * <br>
   * This is the ASCII character code, not the raw data.
   */
  char frameNumber;

  /**
   * The actual transmittable/transmitted data in the frame.
   */
  byte[] data;

  /**
   * Represents the LIS data record of the frame.
   */
  Record record = null;

  /**
   * Constructs a new record based on a byte array (message stream).
   * @param data all data to store including frame number.
   */
  public Frame(byte[] data) {
    this.frameNumber = (char) (data[1]);
    this.data = data;
  }
}
