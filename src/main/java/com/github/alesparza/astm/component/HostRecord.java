package com.github.alesparza.astm.component;

import com.github.alesparza.emulator.gui.InstrumentFrame;

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
   * Gets a Host Record from the InstrumentForm contents
   * @return new Record
   * @param instrumentFrame
   */
  public static HostRecord generateRecord(InstrumentFrame instrumentFrame) {
    HostRecord record = new HostRecord();

    // field 5: Sender Name
    Field field5 = new Field("Sender Name", 1);
    Component component = new Component("Sender Name", 100, instrumentFrame.getDevicePanel().getName().getBytes());
    field5.setComponent(0, component);
    record.setField(5, field5);

    // field 6: Sender Street Address
    Field field6 = new Field("Sender Street Address", 1);
    component = new Component("Address", 100, instrumentFrame.getDevicePanel().getAddress().getBytes());
    field6.setComponent(0, component);
    record.setField(6, field6);

    // field 8: Sender Telephone Number
    Field field8 = new Field("Sender Phone Number", 1);
    component = new Component("Phone", 100, instrumentFrame.getDevicePanel().getPhone().getBytes());
    field8.setComponent(0, component);
    record.setField(8, field8);

    // field 10: Receiver ID
    Field field10 = new Field("Receiver ID", 1);
    component = new Component("Address", 100, "LIS".getBytes());
    field10.setComponent(0, component);
    record.setField(10, field10);
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
