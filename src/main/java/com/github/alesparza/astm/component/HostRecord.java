package com.github.alesparza.astm.component;

import com.github.alesparza.emulator.instrument.InstrumentType;

/**
 * Represents a Host Record.
 * <br>
 * This record only stores information based on the InstrumentType or set by form fields:
 * Instrument Name,
 * Instrument Address,
 * Instrument Telephone Number,
 * Receiver ID.
 */
public class HostRecord extends Record {

  /**
   * Number of fields for Host Records.
   */
  public static final int NUM_FIELDS = 14;


  /**
   * Constructs a new Host Record using instrument specific configurations.
   */
  public HostRecord() {
    super(NUM_FIELDS);
  }

  /**
   * Gets the Record Identifier field of the Host Record (Field 1).
   * @return the requested field.
   */
  public Field getRecordIdentifier() {
    return getField(1);
  }

}
