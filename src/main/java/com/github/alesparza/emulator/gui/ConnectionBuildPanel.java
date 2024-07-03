package com.github.alesparza.emulator.gui;

import com.github.alesparza.emulator.processor.InstrumentConnection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;

public class ConnectionBuildPanel extends JFrame implements ActionListener {
  private JPanel contentPane;
  private JTextField hostnameTextField;
  private JTextField portTextField;
  private JLabel hostnameLabel;
  private JLabel portLabel;
  private JLabel instrumentModeLabel;
  private JLabel titleLabel;
  private JButton connectButton;
  private JComboBox instrumentModeComboBox;

  public ConnectionBuildPanel() {
    setTitle("Instrument Connector");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setContentPane(contentPane);

    // setup connect button
    connectButton.setActionCommand("connect");
    connectButton.addActionListener(this);

    pack();
  }

  public void actionPerformed (ActionEvent e) {
    // handle connection button connecting
    if (e.getActionCommand().equals("connect")) {
      if (instrumentModeComboBox.getSelectedIndex() == 0) {
        System.out.println("Connecting...");
        InstrumentConnection instrumentConnection = new InstrumentConnection();
        instrumentConnection.initialiseSocket(hostnameTextField.getText(), Integer.parseInt(portTextField.getText()));

        // send an ENQ, wait for ACK, close with EOT
        instrumentConnection.sendEnq();

        byte[] response = instrumentConnection.receiveMessage();
        System.out.println(new String(response, StandardCharsets.US_ASCII));

        instrumentConnection.sendAck();
      }
      else if (instrumentModeComboBox.getSelectedItem().equals("Server")) {
        InstrumentConnection instrumentConnection = new InstrumentConnection();
        instrumentConnection.initialiseSocket(Integer.parseInt(portTextField.getText()));
      }
    }


  }
}
