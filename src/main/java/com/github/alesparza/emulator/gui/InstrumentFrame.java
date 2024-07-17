package com.github.alesparza.emulator.gui;

import com.github.alesparza.astm.component.*;
import com.github.alesparza.astm.protcol.AstmProtocol;
import com.github.alesparza.emulator.instrument.Instrument;

import javax.swing.*;

public class InstrumentFrame extends JFrame {
  private JPanel contentPanel;
  private JLabel topLabel;
  private JTabbedPane tabbedPane;
  private JPanel commandsPanel;
  private JScrollPane consoleScrollablePane;
  private JTextArea consoleTextArea;
  private JTextArea commTextArea;
  private JScrollPane commScrollablePane;
  private JScrollPane connectionTab;
  private JScrollPane sampleTab;
  private JScrollPane deviceTab;
  private JScrollPane resultTab;
  private JTextField hostnameTextField;
  private JTextField portTextField;
  private JLabel hostnameLabel;
  private JLabel portLabel;
  private JButton startButton;
  private JButton stopButton;
  private JButton resetButton;
  private JButton checkButton;
  private JScrollPane patientTab;
  private JPanel connectionPanel;
  private PatientPanel patientPanel;
  private JButton sendButton;
  private DevicePanel devicePanel;
  private SamplePanel samplePanel;
  //TODO: add a label or something that changes to indicated connected


  public InstrumentFrame(Instrument instrument) {

    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setContentPane(contentPanel);
    this.hostnameTextField.setText(instrument.getHostname());
    this.portTextField.setText(String.valueOf(instrument.getPort()));
    this.devicePanel.getTypeTextField().setText(instrument.getType().toString());
    instrument.setGUIComponents(consoleTextArea, commTextArea);

    // start button
    this.startButton.addActionListener(e -> instrument.connect());

    // stop button
    this.stopButton.addActionListener(e -> instrument.disconnect());

    // check button
    this.checkButton.addActionListener(e -> instrument.check());

    // reset button
    this.resetButton.addActionListener(e -> instrument.reset());

    // send button
    this.sendButton.addActionListener(e -> {
      HostRecord hostRecord = getHostRecord();
      PatientRecord patientRecord = getPatientRecord();
      OrderRecord orderRecord = getOrderRecord();

      instrument.printConsoleLn("Attempting to send message");

      // start message with ENQ, terminate early on error
      if (!instrument.sendENQ()) {
        instrument.printConsoleLn("Failed to send ENQ");
        instrument.sendEOT();
        return;
      }

      // check for ACK, terminate early if not received
      if (!instrument.checkForACK()) {
        instrument.printConsoleLn("Failed check for ACK");
        instrument.sendEOT();
        return;
      }

      // send H record, terminate early etc.
      int frame = 1; // always start with frame 1
      int lastFrame; // return value is the last frame sent
      if ((lastFrame = instrument.sendHRecord(frame, hostRecord)) == -1) {
        instrument.printConsoleLn("Failed to send H record");
        instrument.sendEOT();
        return;
      }

      frame = (lastFrame + 1) % AstmProtocol.FRAME_MODULO; // calculate next frame to use
      if ((lastFrame = instrument.sendPRecord(frame, patientRecord)) == -1) {
        instrument.printConsoleLn("Failed to send P record");
        instrument.sendEOT();
        return;
      };

      frame = (lastFrame + 1) % AstmProtocol.FRAME_MODULO; // calculate next frame to use
      if ((lastFrame = instrument.sendORecord(frame, orderRecord)) == -1) {
        instrument.printConsoleLn("Failed to send O record");
        instrument.sendEOT();
        return;
      };

      // TODO: need to send C records attached to Order
      // TODO: instrument.sendRCRecords();
      // TODO: instrument.sendLRecord();
      instrument.sendEOT();
    });

    pack();
  }

  /**
   * Gets a Host Record from the InstrumentForm contents
   * @return new Record
   */
  private HostRecord getHostRecord() {
    HostRecord hostRecord = new HostRecord();

    // field 5: Sender Name
    Field field5 = new Field("Sender Name", 1);
    Component component = new Component("Sender Name", 100, devicePanel.getName().getBytes());
    field5.setComponent(0, component);
    hostRecord.setField(5, field5);

    // field 6: Sender Street Address
    Field field6 = new Field("Sender Street Address", 1);
    component = new Component("Address", 100, devicePanel.getAddress().getBytes());
    field6.setComponent(0, component);
    hostRecord.setField(6, field6);

    // field 8: Sender Telephone Number
    Field field8 = new Field("Sender Phone Number", 1);
    component = new Component("Phone", 100, devicePanel.getPhone().getBytes());
    field8.setComponent(0, component);
    hostRecord.setField(8, field8);

    // field 10: Receiver ID
    Field field10 = new Field("Receiver ID", 1);
    component = new Component("Address", 100, "LIS".getBytes());
    field10.setComponent(0, component);
    hostRecord.setField(10, field10);
    return hostRecord;
  }

  /**
   * Gets a Host Record from the InstrumentForm contents
   * @return new Record
   */
  private PatientRecord getPatientRecord() {
    PatientRecord patientRecord = new PatientRecord();

    // field 4: Laboratory Assigned Patient ID
    Field field4 = new Field("Laboratory Assigned Patient ID", 1);
    Component component = new Component("MRN", 100, patientPanel.getPatientID().getBytes());
    field4.setComponent(0, component);
    patientRecord.setField(4, field4);

    // field 6: Patient Name
    Field field6 = new Field("Patient Name", 3);
    component = new Component("Last Name", 100, patientPanel.getLastName().getBytes());
    Component component2 = new Component("First Name", 100, patientPanel.getFirstName().getBytes());
    Component component3 = new Component("Middle Name", 100, patientPanel.getMiddleName().getBytes());
    field6.setComponent(0, component);
    field6.setComponent(1, component2);
    field6.setComponent(2, component3);
    patientRecord.setField(6, field6);

    // field 8: Birthdate (YYYYMMDD)
    Field field8 = new Field("Birthdate", 1);
    component = new Component("DOB", 8, patientPanel.getDOB().getBytes());
    field8.setComponent(0, component);
    patientRecord.setField(8, field8);

    // field 9: Sex
    Field field9 = new Field("Sex", 1);
    component = new Component("Sex", 1, patientPanel.getSex().getBytes());
    field9.setComponent(0, component);
    patientRecord.setField(9, field9);

    // field 26: Location
    Field field26 = new Field("Patient Location", 1);
    component = new Component("Location", 100, patientPanel.getLocation().getBytes());
    field26.setComponent(0, component);
    patientRecord.setField(26, field26);
    return patientRecord;
  }

  /**
   * Gets an Order Record from the InstrumentForm contents
   * @return new Record
   */
  private OrderRecord getOrderRecord() {
    OrderRecord orderRecord = new OrderRecord();

    // field 3: Specimen ID
    Field field3 = new Field("Specimen ID", 1);
    Component component = new Component("Specimen ID", 22, samplePanel.getSpecimenID().getBytes());
    field3.setComponent(0, component);
    orderRecord.setField(3, field3);

    // field 4: Instrument Specimen ID
    Field field4 = new Field("Instrument Specimen ID", 1);
    component = new Component("Instrument Specimen ID", 5, samplePanel.getPositionID().getBytes());
    field4.setComponent(0, component);
    orderRecord.setField(4, field4);

    // field 5: Universal Test ID
    Field field5 = new Field("Universal Test ID", 3);
    // TODO: loop for multiple codes, needs to be built
    component = new Component("Local Test Code", 20, "TEMP".getBytes());
    field5.setComponent(2, component);
    orderRecord.setField(5, field5);

    // field 6: Priority
    Field field6 = new Field("Priority", 1);
    component = new Component("Priority", 1, samplePanel.getPriority().getBytes());
    field6.setComponent(0, component);
    orderRecord.setField(6, field6);

    // field 7: Requested Date and Time
    Field field7 = new Field("Requested Date and Time", 1);
    component = new Component("Requested Date and Time", 14, samplePanel.getRequestDateAndTime().getBytes());
    field7.setComponent(0, component);
    orderRecord.setField(7, field7);

    // field 8: Collected Date and Time
    Field field8 = new Field("Collected Date and Time", 1);
    component = new Component("Collected Date and Time", 14, samplePanel.getCollectionDateAndTime().getBytes());
    field8.setComponent(0, component);
    orderRecord.setField(8, field8);

    // field 15: Received Date and Time
    Field field15 = new Field("Received Date and Time", 1);
    component = new Component("Received Date and Time", 14, samplePanel.getReceivedDateAndTime().getBytes());
    field15.setComponent(0, component);
    orderRecord.setField(15, field15);

    // field 16: Specimen Descriptor
    Field field16 = new Field("Specimen Descriptor", 1);
    component = new Component("Specimen Type", 21, samplePanel.getSpecimenType().getBytes());
    field16.setComponent(0, component);
    orderRecord.setField(16, field16);

    // field 23: Release Date and Time
    Field field23 = new Field("Release Date and Time", 1);
    component = new Component("Release Date and Time", 14, samplePanel.getReleasedDateAndTime().getBytes());
    field23.setComponent(0, component);
    orderRecord.setField(23, field23);

    return orderRecord;
  }
}
