package com.github.alesparza.astm.component;

import com.github.alesparza.emulator.gui.InstrumentFrame;

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

  /**
   * Gets a Host Record from the InstrumentForm contents
   * @return new Record
   * @param instrumentFrame
   */
  public static PatientRecord generateRecord(InstrumentFrame instrumentFrame) {
    PatientRecord record = new PatientRecord();

    // field 4: Laboratory Assigned Patient ID
    Field field4 = new Field("Laboratory Assigned Patient ID", 1);
    Component component = new Component("MRN", 100, instrumentFrame.getPatientPanel().getPatientID().getBytes());
    field4.setComponent(0, component);
    record.setField(4, field4);

    // field 6: Patient Name
    Field field6 = new Field("Patient Name", 3);
    component = new Component("Last Name", 100, instrumentFrame.getPatientPanel().getLastName().getBytes());
    Component component2 = new Component("First Name", 100, instrumentFrame.getPatientPanel().getFirstName().getBytes());
    Component component3 = new Component("Middle Name", 100, instrumentFrame.getPatientPanel().getMiddleName().getBytes());
    field6.setComponent(0, component);
    field6.setComponent(1, component2);
    field6.setComponent(2, component3);
    record.setField(6, field6);

    // field 8: Birthdate (YYYYMMDD)
    Field field8 = new Field("Birthdate", 1);
    component = new Component("DOB", 8, instrumentFrame.getPatientPanel().getDOB().getBytes());
    field8.setComponent(0, component);
    record.setField(8, field8);

    // field 9: Sex
    Field field9 = new Field("Sex", 1);
    component = new Component("Sex", 1, instrumentFrame.getPatientPanel().getSex().getBytes());
    field9.setComponent(0, component);
    record.setField(9, field9);

    // field 26: Location
    Field field26 = new Field("Patient Location", 1);
    component = new Component("Location", 100, instrumentFrame.getPatientPanel().getLocation().getBytes());
    field26.setComponent(0, component);
    record.setField(26, field26);
    return record;
  }
}
