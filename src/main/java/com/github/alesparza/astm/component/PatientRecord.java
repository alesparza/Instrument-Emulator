package com.github.alesparza.astm.component;

/**
 * Represents a Patient Record.
 * <br>
 * This record only stores information based on the InstrumentType or set by form fields:
 * Laboratory Assigned Patient ID,
 * Patient Name,
 * Birthdate,
 * Sex,
 * Location.
 */
public class PatientRecord extends Record {

  /**
   * Number of fields for Host Records.
   */
  public static final int NUM_FIELDS = 34;


  /**
   * Constructs a new Host Record using instrument specific configurations.
   */
  public PatientRecord() {
    super(NUM_FIELDS);
  }

}
