package com.github.alesparza.emulator.gui;

import com.github.alesparza.emulator.processor.Emulator;
import com.github.alesparza.emulator.processor.Instrument;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;

public class GUI extends JFrame implements ActionListener {
  private JPanel contentPane;
  private JTextField hostnameTextField;
  private JTextField portTextField;
  private JLabel hostnameLabel;
  private JLabel portLabel;
  private JLabel instrumentModeLabel;
  private JLabel titleLabel;
  private JButton connectButton;
  private JComboBox comboBox1;

  public GUI () {
    setTitle("Emulator");
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
      if (comboBox1.getSelectedIndex() == 0) {
        System.out.println("Connecting...");
        Instrument instrument = new Instrument();
        instrument.initialiseSocket(hostnameTextField.getText(), Integer.parseInt(portTextField.getText()));

        // send an ENQ, wait for ACK, close with EOT
        instrument.sendEnq();

        byte[] response = instrument.receiveMessage();
        System.out.println(new String(response, StandardCharsets.US_ASCII));

        instrument.sendAck();
      }
      else if (comboBox1.getSelectedItem().equals("Server")) {
        Instrument instrument = new Instrument();
        instrument.initialiseSocket(Integer.parseInt(portTextField.getText()));
      }
    }


  }
}
