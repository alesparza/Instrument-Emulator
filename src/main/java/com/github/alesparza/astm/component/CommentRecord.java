package com.github.alesparza.astm.component;

/**
 * Represents a Comment Record.
 * <br>
 * This record only stores information based on the InstrumentType or set by form fields:
 * TODO: Add
 */
public class CommentRecord extends Record {

  /**
   * Number of fields for Host Records.
   */
  public static final int NUM_FIELDS = 5;


  /**
   * Constructs a new Host Record using instrument specific configurations.
   */
  public CommentRecord() {
    super(NUM_FIELDS);
  }

}
