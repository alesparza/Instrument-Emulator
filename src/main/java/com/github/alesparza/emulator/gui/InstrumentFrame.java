package com.github.alesparza.emulator.gui;

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
  private JScrollPane resultTab;
  private JTextField hostnameTextField;
  private JTextField portTextField;
  private JTextField typeTextField;
  private JLabel hostnameLabel;
  private JLabel portLabel;
  private JLabel typeLabel;
  private JButton startButton;
  private JButton stopButton;
  private JButton resetButton;
  private JButton checkButton;


  private Instrument instrument;

  public InstrumentFrame(Instrument instrument) {
    this.instrument = instrument;
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setContentPane(contentPanel);
    this.hostnameTextField.setText(instrument.getHostname());
    this.portTextField.setText(String.valueOf(instrument.getPort()));
    this.typeTextField.setText(instrument.getType().toString());

    // start button
    this.startButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        printConsoleLn(instrument.connect());
      }
    });

    // stop button
    this.stopButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        printConsoleLn(instrument.disconnect());
      }
    });


    pack();
  }


  public void printConsoleLn(String message) {
    this.consoleTextArea.append(message + "\n");
    this.consoleTextArea.setCaretPosition(this.consoleTextArea.getDocument().getLength());
  }

  public void printCommLn(String message) {
    this.commTextArea.append(message + "\n");
    this.commTextArea.setCaretPosition(this.commTextArea.getDocument().getLength());
  }
}
