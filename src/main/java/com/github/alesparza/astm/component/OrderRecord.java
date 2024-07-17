package com.github.alesparza.astm.component;

/**
 * Represents an order Record.
 * <br>
 * This record only stores information based on the InstrumentType or set by form fields:
 * Specimen ID,
 * Position ID,
 * Priority,
 * Request, Collection, Received, and Released Date/Time,
 * Specimen Type.
 */
public class OrderRecord extends Record {

  /**
   * Number of fields for Host Records.
   */
  public static final int NUM_FIELDS = 31;


  /**
   * Constructs a new Host Record using instrument specific configurations.
   */
  public OrderRecord() {
    super(NUM_FIELDS);
  }

}
