package com.github.alesparza.astm.component;

/**
 * Represents a Terminator Record.
 */
public class TerminatorRecord extends Record {

  /**
   * Number of fields for Host Records.
   */
  public static final int NUM_FIELDS = 3;


  /**
   * Constructs a new Host Record using instrument specific configurations.
   */
  public TerminatorRecord() {
    super(NUM_FIELDS);
  }

  /**
   * Gets a Terminator Record
   * @return new Record
   */
  public static TerminatorRecord generateRecord() {
    TerminatorRecord record = new TerminatorRecord();
    return record;
  }

  /**
   * Gets the Record Identifier field of the Host Record (Field 1).
   * @return the requested field.
   */
  public Field getRecordIdentifier() {
    return getField(1);
  }

}
