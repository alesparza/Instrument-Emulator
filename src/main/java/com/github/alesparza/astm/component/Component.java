package com.github.alesparza.astm.component;

import java.util.Arrays;

/**
 * Represents a sub-field in an ASTM record.
 */
public class Component {

  /**
   * Name of this component
   */
  private String name;

  /**
   * Max length of data in this component.
   */
  private int maxLength;

  /**
   * The data in this component.
   */
  private byte[] data;

  /**
   * Constructs a new component.
   * @param name name of the component
   * @param maxLength maximum length of the component
   * @param data the data stored in the component.
   */
  public Component(String name, int maxLength, byte[] data) {
    this.name = name;
    this.maxLength = maxLength;
    this.data = data;

    if (data.length > maxLength) {
      this.data = Arrays.copyOfRange(data,0, maxLength);
    }
  }

  /**
   * Constructs a new component.
   * @param maxLength maximum length of the component
   * @param data the data stored in the component.
   */
  public Component(int maxLength, byte[] data) {
    this("", maxLength, data);
  }

  /**
   * Gets the name of this component.
   * @return the name of this component
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the max length of this component.
   * @return the max length of this component
   */
  public int getMaxLength() {
    return maxLength;
  }

  /**
   * Gets the data of this component.
   * @return the data of this component
   */
  public byte[] getData() {
    return data;
  }

  /**
   * Gets the data length of this component.
   * @return the data length of this component
   */
  public int getDataLength() {
    return data.length;
  }

}
