package com.github.alesparza.emulator.gui;

import com.github.alesparza.astm.component.Component;
import com.github.alesparza.astm.component.Field;
import com.github.alesparza.astm.component.HostRecord;
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
  private JTextField typeTextField; // TODO: move to device panel
  private JLabel hostnameLabel;
  private JLabel portLabel;
  private JLabel typeLabel;
  private JButton startButton;
  private JButton stopButton;
  private JButton resetButton;
  private JButton checkButton;
  private JScrollPane patientTab;
  private JPanel connectionPanel;
  private PatientPanel patientPanel;
  private JButton sendButton;
  //TODO: add a label or something that changes to indicated connected


  public InstrumentFrame(Instrument instrument) {

    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setContentPane(contentPanel);
    this.hostnameTextField.setText(instrument.getHostname());
    this.portTextField.setText(String.valueOf(instrument.getPort()));
    this.typeTextField.setText(instrument.getType().toString());
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

      // TODO: build all records based on current state of form
      HostRecord hostRecord = new HostRecord();

      // field 5: Sender Name
      Field field5 = new Field("Sender Name", 1);
      Component component = new Component("Sender Name", 100, "DxH".getBytes());
      field5.setComponent(0, component);
      hostRecord.setField(5, field5);

      // field 6: Sender Street Address
      Field field6 = new Field("Sender Street Address", 1);
      component = new Component("Address", 100, "123 Lab Lane".getBytes());
      field6.setComponent(0, component);
      hostRecord.setField(6, field6);

      // field 8: Sender Telephone Number
      Field field8 = new Field("Sender Phone Number", 1);
      component = new Component("Phone", 100, "555.123.4567".getBytes());
      field8.setComponent(0, component);
      hostRecord.setField(8, field8);

      // field 10: Receiver ID
      Field field10 = new Field("Receiver ID", 1);
      component = new Component("Address", 100, "LIS".getBytes());
      field10.setComponent(0, component);
      hostRecord.setField(10, field10);

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
      // TODO: instrument.sendPRecord();
      // TODO: instrument.sendORecord();
      // TODO: instrument.sendRCRecords();
      // TODO: instrument.sendLRecord();
      instrument.sendEOT();
    });

    pack();
  }
}
