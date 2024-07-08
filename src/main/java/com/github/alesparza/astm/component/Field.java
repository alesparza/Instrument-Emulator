package com.github.alesparza.astm.component;

import java.util.ArrayList;

/**
 * Represents a field in an ASTM record.
 */
public class Field {

  /**
   * List of components in this field.
   */
  private Component[] components;

  /**
   * Name of this field.
   */
  private String name = "";


  /**
   * Constructs a new Field with the specified number of components.
   * @param numComponents the number of components in this field.
   */
  public Field(int numComponents) {
    components = new Component[numComponents];
  }

  /**
   * Constructs a new Field with the specified number of components and a specific name.
   * @param numComponents the number of components in this field
   * @param name the name of this field
   */
  public Field(int numComponents, String name) {
    components = new Component[numComponents];
    this.name = name;
  }

  /**
   * Constructs a new Field with the specified number of components and a specific name.
   * @param name the name of this field
   * @param numComponents the number of components in this field
   */
  public Field(String name, int numComponents) {
    this(numComponents, name);
  }

  /**
   * Gets a specific component.
   * @param idx zero-indexed component
   * @return the field at the specified index, or <code>null</code> if the index is invalid
   */
  public Component getComponent(int idx) {
    if (idx < 0 || idx >= components.length) {
      return null;
    }
    return components[idx];
  }

  /**
   * Sets a specific component.
   * @param idx zero-indexed component
   * @param component the Component to store
   */
  public void setComponent(int idx, Component component) {
    components[idx] = component;
  }

}
