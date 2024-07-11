package com.github.alesparza.emulator.gui;

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
      instrument.printConsoleLn("Attempting to send message");
      instrument.sendENQ();
      // TODO: instrument.sendHRecord();
      // TODO: instrument.sendPRecord();
      // TODO: instrument.sendORecord();
      // TODO: instrument.sendRCRecords();
      // TODO: instrument.sendLRecord();
      instrument.sendEOT();
    });

    pack();
  }
}
