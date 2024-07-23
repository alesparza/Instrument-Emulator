package com.github.alesparza.astm.component;

import com.github.alesparza.emulator.gui.InstrumentFrame;

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

  /**
   * Gets an Order Record from the InstrumentForm contents
   * @return new Record
   * @param instrumentFrame
   */
  public static OrderRecord generateRecord(InstrumentFrame instrumentFrame) {
    OrderRecord record = new OrderRecord();

    // field 3: Specimen ID
    Field field3 = new Field("Specimen ID", 1);
    Component component = new Component("Specimen ID", 22, instrumentFrame.getSamplePanel().getSpecimenID().getBytes());
    field3.setComponent(0, component);
    record.setField(3, field3);

    // field 4: Instrument Specimen ID
    Field field4 = new Field("Instrument Specimen ID", 1);
    component = new Component("Instrument Specimen ID", 5, instrumentFrame.getSamplePanel().getPositionID().getBytes());
    field4.setComponent(0, component);
    record.setField(4, field4);

    // field 5: Universal Test ID
    Field field5 = new Field("Universal Test ID", 3);
    // TODO: loop for multiple codes, needs to be built
    component = new Component("Local Test Code", 20, "TEMP".getBytes());
    field5.setComponent(2, component);
    record.setField(5, field5);

    // field 6: Priority
    Field field6 = new Field("Priority", 1);
    component = new Component("Priority", 1, instrumentFrame.getSamplePanel().getPriority().getBytes());
    field6.setComponent(0, component);
    record.setField(6, field6);

    // field 7: Requested Date and Time
    Field field7 = new Field("Requested Date and Time", 1);
    component = new Component("Requested Date and Time", 14, instrumentFrame.getSamplePanel().getRequestDateAndTime().getBytes());
    field7.setComponent(0, component);
    record.setField(7, field7);

    // field 8: Collected Date and Time
    Field field8 = new Field("Collected Date and Time", 1);
    component = new Component("Collected Date and Time", 14, instrumentFrame.getSamplePanel().getCollectionDateAndTime().getBytes());
    field8.setComponent(0, component);
    record.setField(8, field8);

    // field 15: Received Date and Time
    Field field15 = new Field("Received Date and Time", 1);
    component = new Component("Received Date and Time", 14, instrumentFrame.getSamplePanel().getReceivedDateAndTime().getBytes());
    field15.setComponent(0, component);
    record.setField(15, field15);

    // field 16: Specimen Descriptor
    Field field16 = new Field("Specimen Descriptor", 1);
    component = new Component("Specimen Type", 21, instrumentFrame.getSamplePanel().getSpecimenType().getBytes());
    field16.setComponent(0, component);
    record.setField(16, field16);

    // field 23: Release Date and Time
    Field field23 = new Field("Release Date and Time", 1);
    component = new Component("Release Date and Time", 14, instrumentFrame.getSamplePanel().getReleasedDateAndTime().getBytes());
    field23.setComponent(0, component);
    record.setField(23, field23);

    return record;
  }
}
