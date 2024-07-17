package com.github.alesparza.astm.component;

/**
 * Represents a Result Record.
 * <br>
 * This record only stores information based on the InstrumentType or set by form fields:
 * TODO: ADD
 */
public class ResultRecord extends Record {

  /**
   * Number of fields for Host Records.
   */
  public static final int NUM_FIELDS = 15;


  /**
   * Constructs a new Host Record using instrument specific configurations.
   */
  public ResultRecord() {
    super(NUM_FIELDS);
  }

}
