package com.github.alesparza.emulator.gui;

import com.github.alesparza.astm.component.*;
import com.github.alesparza.astm.protcol.AstmProtocol;
import com.github.alesparza.emulator.assay.Assay;
import com.github.alesparza.emulator.instrument.Instrument;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
  private ResultPanel resultPanel;
  private JScrollPane resultTab;
  private JButton clearButton;
  //TODO: add a label or something that changes to indicated connected


  public InstrumentFrame(Instrument instrument) {

    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setContentPane(contentPanel);
    this.resultPanel.setGUIComponents(consoleTextArea);
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

    this.clearButton.addActionListener(e -> {
      consoleTextArea.setText("");
      commTextArea.setText("");
    });

    // send button
    this.sendButton.addActionListener(e -> {
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
      HostRecord hostRecord = HostRecord.generateRecord(this);
      int frame = 1; // always start with frame 1
      int lastFrame; // return value is the last frame sent
      if ((lastFrame = instrument.sendHRecord(frame, hostRecord)) == -1) {
        instrument.printConsoleLn("Failed to send H record");
        instrument.sendEOT();
        return;
      }

      PatientRecord patientRecord = PatientRecord.generateRecord(this);
      frame = (lastFrame + 1) % AstmProtocol.FRAME_MODULO; // calculate next frame to use
      if ((lastFrame = instrument.sendPRecord(frame, patientRecord)) == -1) {
        instrument.printConsoleLn("Failed to send P record");
        instrument.sendEOT();
        return;
      };

      OrderRecord orderRecord = OrderRecord.generateRecord(this);
      frame = (lastFrame + 1) % AstmProtocol.FRAME_MODULO; // calculate next frame to use
      if ((lastFrame = instrument.sendORecord(frame, orderRecord)) == -1) {
        instrument.printConsoleLn("Failed to send O record");
        instrument.sendEOT();
        return;
      };

      // TODO: send C records attached to order

      // TODO: send R and C records in a loop
      int sequence = 1;
      int lastSequence = 1;
      for (Assay assay : resultPanel.assayArrayList) {
        ResultRecord resultRecord = ResultRecord.generateRecord(this, assay);
        frame = (lastFrame + 1) % AstmProtocol.FRAME_MODULO; // calculate next frame to use
        // TODO: calculate the sequence to use
        if ((sequence = instrument.sendRRecord(frame, lastSequence, resultRecord)) == -1) {
          instrument.printConsoleLn("Failed to send R record");
          instrument.sendEOT();
          return;
        };
        // can't return two variables, but they grow together
        // sequence number does not reset, so check the difference to know what the new lastFrame is
        lastFrame = lastFrame + (sequence - lastSequence + 1);
        lastSequence = sequence + 1;
        // TODO: another loop to send C records for this result
      }


      TerminatorRecord terminatorRecord = TerminatorRecord.generateRecord();
      frame = (lastFrame + 1) % AstmProtocol.FRAME_MODULO; // calculate next frame to use
      if ((lastFrame = instrument.sendLRecord(frame, terminatorRecord)) == -1) {
        instrument.printConsoleLn("Failed to send L record");
        instrument.sendEOT();
        return;
      };

      instrument.sendEOT();
    });

    pack();
  }

  public JPanel getConnectionPanel() {
    return connectionPanel;
  }

  public PatientPanel getPatientPanel() {
    return patientPanel;
  }

  public DevicePanel getDevicePanel() {
    return devicePanel;
  }

  public SamplePanel getSamplePanel() {
    return samplePanel;
  }

  public ResultPanel getResultPanel() {
    return resultPanel;
  }
}
