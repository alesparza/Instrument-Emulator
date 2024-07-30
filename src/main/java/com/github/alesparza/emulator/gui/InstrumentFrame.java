package com.github.alesparza.emulator.gui;

import com.github.alesparza.astm.component.*;
import com.github.alesparza.astm.protcol.AstmProtocol;
import com.github.alesparza.emulator.assay.Assay;
import com.github.alesparza.emulator.instrument.Instrument;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

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
  private JButton listenButton;
  //TODO: add a label or something that changes to indicated connected


  public InstrumentFrame(Instrument instrument) {

    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setContentPane(contentPanel);
    this.resultPanel.setGUIComponents(consoleTextArea);
    this.hostnameTextField.setText(instrument.getHostname());
    this.portTextField.setText(String.valueOf(instrument.getPort()));
    this.devicePanel.getTypeTextField().setText(instrument.getType().toString());
    instrument.setGUIComponents(consoleTextArea, commTextArea);
    if (instrument.getHostname().isEmpty()) listenButton.setEnabled(true);

    // start button
    this.startButton.addActionListener(e -> instrument.connect());

    // stop button
    this.stopButton.addActionListener(e -> instrument.disconnect());

    // listen button
    this.listenButton.addActionListener(e -> instrument.listen());

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
      }
      ;

      OrderRecord orderRecord = OrderRecord.generateRecord(this);
      frame = (lastFrame + 1) % AstmProtocol.FRAME_MODULO; // calculate next frame to use
      if ((lastFrame = instrument.sendORecord(frame, orderRecord)) == -1) {
        instrument.printConsoleLn("Failed to send O record");
        instrument.sendEOT();
        return;
      }
      ;

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
        }
        ;
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
      }
      ;

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

  {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
    $$$setupUI$$$();
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer
   * >>> IMPORTANT!! <<<
   * DO NOT edit this method OR call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    contentPanel = new JPanel();
    contentPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
    contentPanel.setEnabled(true);
    topLabel = new JLabel();
    topLabel.setText("Instrument");
    contentPanel.add(topLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    tabbedPane = new JTabbedPane();
    contentPanel.add(tabbedPane, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(800, 300), null, 0, false));
    tabbedPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Emulation", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    connectionTab = new JScrollPane();
    connectionTab.setMaximumSize(new Dimension(800, 400));
    connectionTab.setMinimumSize(new Dimension(400, 100));
    tabbedPane.addTab("Connection", null, connectionTab, "Connection information");
    connectionPanel = new JPanel();
    connectionPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
    connectionTab.setViewportView(connectionPanel);
    hostnameLabel = new JLabel();
    hostnameLabel.setText("Hostname");
    connectionPanel.add(hostnameLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    hostnameTextField = new JTextField();
    hostnameTextField.setEditable(false);
    connectionPanel.add(hostnameTextField, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), new Dimension(150, -1), 0, false));
    portLabel = new JLabel();
    portLabel.setText("Port");
    connectionPanel.add(portLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    portTextField = new JTextField();
    portTextField.setEditable(false);
    connectionPanel.add(portTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(40, -1), new Dimension(80, -1), 0, false));
    final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
    connectionPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
    final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
    connectionPanel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    deviceTab = new JScrollPane();
    tabbedPane.addTab("Device", null, deviceTab, "Device information");
    devicePanel = new DevicePanel();
    deviceTab.setViewportView(devicePanel.$$$getRootComponent$$$());
    patientTab = new JScrollPane();
    patientTab.setVerticalScrollBarPolicy(20);
    tabbedPane.addTab("Patient", null, patientTab, "Patient information");
    patientPanel = new PatientPanel();
    patientTab.setViewportView(patientPanel.$$$getRootComponent$$$());
    sampleTab = new JScrollPane();
    tabbedPane.addTab("Sample", null, sampleTab, "Sample information");
    samplePanel = new SamplePanel();
    sampleTab.setViewportView(samplePanel.$$$getRootComponent$$$());
    resultTab = new JScrollPane();
    resultTab.setVerticalScrollBarPolicy(20);
    tabbedPane.addTab("Results", resultTab);
    resultPanel = new ResultPanel();
    resultTab.setViewportView(resultPanel.$$$getRootComponent$$$());
    commandsPanel = new JPanel();
    commandsPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 8, new Insets(0, 0, 0, 0), -1, -1));
    contentPanel.add(commandsPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(356, 53), null, 0, false));
    commandsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Commands", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    startButton = new JButton();
    startButton.setText("Start");
    startButton.setToolTipText("Connect the instrument");
    commandsPanel.add(startButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    stopButton = new JButton();
    stopButton.setText("Stop");
    stopButton.setToolTipText("Disconnect the instrument.");
    commandsPanel.add(stopButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    resetButton = new JButton();
    resetButton.setText("Reset");
    resetButton.setToolTipText("Reset the connections.");
    commandsPanel.add(resetButton, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    checkButton = new JButton();
    checkButton.setText("Check");
    checkButton.setToolTipText("Send an ENQ to check connection");
    commandsPanel.add(checkButton, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
    commandsPanel.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(0, 7, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    sendButton = new JButton();
    sendButton.setText("Send");
    sendButton.setToolTipText("Send message based on all data in tabs below.");
    commandsPanel.add(sendButton, new com.intellij.uiDesigner.core.GridConstraints(0, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    clearButton = new JButton();
    clearButton.setText("Clear Comms");
    commandsPanel.add(clearButton, new com.intellij.uiDesigner.core.GridConstraints(0, 6, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    listenButton = new JButton();
    listenButton.setEnabled(false);
    listenButton.setText("Listen");
    commandsPanel.add(listenButton, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    consoleScrollablePane = new JScrollPane();
    consoleScrollablePane.setVerticalScrollBarPolicy(20);
    contentPanel.add(consoleScrollablePane, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(800, 150), null, 0, false));
    consoleScrollablePane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Console Log", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    consoleTextArea = new JTextArea();
    consoleScrollablePane.setViewportView(consoleTextArea);
    commScrollablePane = new JScrollPane();
    contentPanel.add(commScrollablePane, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(800, 150), null, 0, false));
    commScrollablePane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Communication Log", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    commTextArea = new JTextArea();
    commScrollablePane.setViewportView(commTextArea);
    hostnameLabel.setLabelFor(hostnameTextField);
    portLabel.setLabelFor(portTextField);
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return contentPanel;
  }
}
