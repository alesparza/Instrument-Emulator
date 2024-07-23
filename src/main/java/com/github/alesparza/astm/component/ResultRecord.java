package com.github.alesparza.astm.component;

import com.github.alesparza.emulator.assay.Assay;
import com.github.alesparza.emulator.gui.InstrumentFrame;

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

  /**
   * Gets an Order Record from the InstrumentForm contents
   * @return new Record
   * @param instrumentFrame
   */
  public static ResultRecord generateRecord(InstrumentFrame instrumentFrame, Assay assay) {
    ResultRecord record = new ResultRecord();
    Component component = null;

    // field 3: Universal Test ID
    Field field3 = new Field("Universal Test ID", 4);
    Component component3 = new Component("Local Code: Test", 20, assay.getCode().getBytes());
    field3.setComponent(2, component3);
    record.setField(3, field3);

    // field 4: Instrument Specimen ID
    Field field4 = new Field("Data Measurement/Value", 2);
    component = new Component("Value", 10, assay.getResult().getBytes());
    field4.setComponent(0, component);
    record.setField(4, field4);

    // field 5: Units
    Field field5 = new Field("Units", 2);
    component = new Component("Units", 15, assay.getUnits().getBytes());
    field5.setComponent(0, component);
    record.setField(5, field5);

    // field 14: Complete Date and Time
    Field field14 = new Field("Completed Date and Time", 1);
    component = new Component("Completed Date and Time", 14, assay.getCompleteDateTime().getBytes());
    field14.setComponent(0, component);
    record.setField(14, field14);

    return record;
  }

}
