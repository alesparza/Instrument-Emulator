package com.github.alesparza.astm.component;

import com.github.alesparza.emulator.instrument.InstrumentType;

/**
 * Represents a Host Record.
 */
public class HostRecord extends Record {

  /**
   * Number of fields for Host Records.
   */
  public static final int NUM_FIELDS = 14;


  /**
   * Constructs a new Host Record using instrument specific configurations.
   * @param type the instrument type to use to construct the record.
   */
  public HostRecord(InstrumentType type) {
    super(NUM_FIELDS);
    //TODO: insert instrument-agnostic configurations here
    Field recordIdentifier = new Field(1, "Record Identifier");
    Component component = new Component("Record Identifier", 1, new byte[] { (byte) RecordType.H.getRecordIdentifierChar()});
    recordIdentifier.setComponent(0, component);
    fields[1] = recordIdentifier;

    Field delimiters = new Field(4, "Delimiters");

    // remainder are instrument specific
    switch (type) {

      case GENERIC -> {
        //TODO: make generic fields without size restrictions
        ;
      }
      case DxH -> {
        //TODO: make fields based on DxH specification
        ;
      }
      default -> throw new IllegalStateException("Unsupported instrument type: " + type);
    }
  }

  /**
   * Gets the Record Identifier field of the Host Record (Field 1).
   * @return the requested field.
   */
  public Field getRecordIdentifier() {
    return getField(1);
  }
}
