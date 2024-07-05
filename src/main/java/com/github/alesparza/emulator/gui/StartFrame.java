package com.github.alesparza.emulator.gui;

import com.github.alesparza.emulator.instrument.Instrument;
import com.github.alesparza.emulator.instrument.InstrumentType;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StartFrame extends JFrame {


  private JPanel contentPane;
  private JLabel topLabel;
  private JTable instrumentsTable;
  private JComboBox<InstrumentType> instrumentTypeComboBox;
  private JTextField nameTextField;
  private JLabel nameLabel;
  private JLabel instrumentTypeLabel;
  private JLabel connectionTypeLabel;
  private JLabel portLabel;
  private JLabel hostnameLabel;
  private JTextField hostnameTextField;
  private JTextField portTextField;
  private JButton createInstrumentButton;
  private JRadioButton serverRadioButton;
  private JRadioButton clientRadioButton;
  private JTextArea consoleTextArea;
  private JPanel instrumentCreationPanel;
  private JScrollPane consolePanel;
  private JScrollPane instrumentsPanel;
  private final TableModel instrumentTableModel;


  private final ArrayList<Instrument> instrumentArrayList = new ArrayList<>();

  public StartFrame() {
    setTitle("Emulator");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setContentPane(contentPane);

    // add instrument types to dropdown
    for (InstrumentType instrumentType : InstrumentType.values()) {
      instrumentTypeComboBox.addItem(instrumentType);
    }

    // Setup instruments table
    instrumentTableModel = new AbstractTableModel() {
      private final String[] columnNames = {"Name", "Type", "Connection Type", "Hostname/IP", "Port Number"};
      @Override
      public int getRowCount() {
        return instrumentArrayList.size();
      }

      @Override
      public int getColumnCount() {
        return columnNames.length;
      }

      @Override
      public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
          case 0:
            return instrumentArrayList.get(rowIndex).getName();
          case 1:
            return instrumentArrayList.get(rowIndex).getType();
          case 2:
            if (instrumentArrayList.get(rowIndex).getHostname().isEmpty()) {
              return "Server";
            }
            else return "Client";
          case 3:
            return instrumentArrayList.get(rowIndex).getHostname();
          case 4:
            return instrumentArrayList.get(rowIndex).getPort();
        }
        return "";
      }

      @Override
      public String getColumnName(int column) {
        return columnNames[column];
      }
    };
    instrumentsTable.setModel(instrumentTableModel);



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

        // servers can't use the same port
        if (serverRadioButton.isSelected()) {
          for (Instrument instrument : instrumentArrayList) {
            if (instrument.getPort() == port) {
              println("Already have server type instrument listening on port " + port);
              return;
            }
          }
        }

        if (clientRadioButton.isSelected()) {
          for (Instrument instrument : instrumentArrayList) {
            if (instrument.getPort() == port && instrument.getHostname().equals(hostname)) {
              println("Already have client type instrument connecting on " + hostname + ":" + port);
              return;
              //TODO: collapse loop into single pass
            }
          }
        }
        // this is hardcoded so it should not be an issue
        type  = (InstrumentType) instrumentTypeComboBox.getSelectedItem();

        // add new instrument to table
        Instrument instrument = new Instrument(name, type, hostname, port);
        instrumentArrayList.add(instrument);
        println("Created new instrument '" + name + "' of type " + type + ", communicating on " + hostname + ":" + port);
        ((AbstractTableModel) instrumentTableModel).fireTableDataChanged();

        //create a new window to open
        JFrame instrumentedFrame = new InstrumentFrame(instrument);
        instrumentedFrame.setTitle(name);
        instrumentedFrame.setLocationRelativeTo(null);
        instrumentedFrame.setVisible(true);
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
    this.consoleTextArea.setCaretPosition(this.consoleTextArea.getDocument().getLength());
  }
}
