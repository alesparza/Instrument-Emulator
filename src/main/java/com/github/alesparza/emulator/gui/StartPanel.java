package com.github.alesparza.emulator.gui;

import com.github.alesparza.emulator.instrument.Instrument;
import com.github.alesparza.emulator.instrument.InstrumentType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StartPanel extends JFrame {


  private JPanel contentPane;
  private JLabel topLabel;
  private JTable instrumentsTable;
  private JComboBox instrumentTypeComboBox;
  private JTextField nameTextField;
  private JLabel nameLabel;
  private JLabel instrumentTypeLabel;
  private JLabel conectionTypeLabel;
  private JLabel portLabel;
  private JLabel hostnameLabel;
  private JTextField hostnameTextField;
  private JTextField portTextField;
  private JButton createInstrumentButton;
  private JRadioButton serverRadioButton;
  private JRadioButton clientRadioButton;
  private JTextArea consoleTextArea;
  private JPanel instrumentCreationPanel;
  private JPanel consolePanel;
  private JPanel createdInstrumentsPanel;


  private final ArrayList<Instrument> instrumentArrayList = new ArrayList<>();

  public StartPanel() {
    setTitle("Emulator");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setContentPane(contentPane);

    // add instrument types to dropdown
    for (InstrumentType instrumentType : InstrumentType.values()) {
      instrumentTypeComboBox.addItem(instrumentType);
    }

    // create a new instrument
    createInstrumentButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        InstrumentType type;
        String name;
        String hostname;
        int port;

        // make sure all required fields filled out
        if (nameTextField.getText().isEmpty()) {
          println("Instrument name cannot be blank.");
          return;
        }
        name = nameTextField.getText();
        for (Instrument instrument : instrumentArrayList) {
          if (instrument.getName().equals(name)) {
            println("Instrument name already exits");
            return;
          }
        }

        if (hostnameTextField.getText().isEmpty() && clientRadioButton.isSelected()) {
          println("Hostname/IP cannot be blank when running as client.");
          return;
        }
        if (serverRadioButton.isSelected()) {
          hostname = "";
        } else {
          hostname = hostnameTextField.getText();
        }

        if (portTextField.getText().isEmpty()) {
          println("Port number required.");
          return;
        }
        try {
          port = Integer.parseInt(portTextField.getText());
        } catch (NumberFormatException ex) {
          println("Port number must be an integer");
          return;
        }
        type  = (InstrumentType) instrumentTypeComboBox.getSelectedItem();

        Instrument instrument = new Instrument(name, type, hostname, port);
        instrumentArrayList.add(instrument);
        println("Created new instrument '" + name + "' of type " + type + ", communicating on " + hostname + ":" + port);
      }
    });


    // can only select one option
    serverRadioButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        clientRadioButton.setSelected(false);
        hostnameTextField.setEditable(false);
      }
    });
    clientRadioButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        serverRadioButton.setSelected(false);
        hostnameTextField.setEditable(true);
      }
    });

    // done setting up GUI
    pack();
    println("Setup complete");
  }


  public void println(String message) {
    this.consoleTextArea.append(message + "\n");
  }
}
