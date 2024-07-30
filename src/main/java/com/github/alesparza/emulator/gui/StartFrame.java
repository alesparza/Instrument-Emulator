package com.github.alesparza.emulator.gui;

import com.github.alesparza.emulator.instrument.Instrument;
import com.github.alesparza.emulator.instrument.InstrumentType;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
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
  private JPanel reservedPanel;
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
            } else return "Client";
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
    createInstrumentButton.addActionListener(e -> {
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
      type = (InstrumentType) instrumentTypeComboBox.getSelectedItem();

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
      // TODO: when this frame closes, remove from the instruments table
    });

    // can only select one option
    serverRadioButton.addActionListener(e -> {
      clientRadioButton.setSelected(false);
      hostnameTextField.setEditable(false);
      serverRadioButton.setEnabled(false);
      clientRadioButton.setEnabled(true);
    });
    clientRadioButton.addActionListener(e -> {
      if (!clientRadioButton.isSelected()) {
        return;
      }
      serverRadioButton.setSelected(false);
      hostnameTextField.setEditable(true);
      serverRadioButton.setEnabled(true);
      clientRadioButton.setEnabled(false);
    });

    // done setting up GUI
    pack();
    println("Setup complete");
  }


  public void println(String message) {
    this.consoleTextArea.append(message + "\n");
    this.consoleTextArea.setCaretPosition(this.consoleTextArea.getDocument().getLength());
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
    contentPane = new JPanel();
    contentPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
    topLabel = new JLabel();
    topLabel.setHorizontalAlignment(2);
    topLabel.setHorizontalTextPosition(11);
    topLabel.setText("Emulator");
    contentPane.add(topLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    instrumentCreationPanel = new JPanel();
    instrumentCreationPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 3, new Insets(0, 0, 0, 0), -1, -1));
    instrumentCreationPanel.setName("Instrument Creation");
    contentPane.add(instrumentCreationPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    instrumentCreationPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Instrument Creation", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, null, null));
    nameLabel = new JLabel();
    nameLabel.setText("Name");
    instrumentCreationPanel.add(nameLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    nameTextField = new JTextField();
    nameTextField.setText("enter instrument name here");
    instrumentCreationPanel.add(nameTextField, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
    instrumentTypeLabel = new JLabel();
    instrumentTypeLabel.setText("Instrument Type");
    instrumentCreationPanel.add(instrumentTypeLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    instrumentTypeComboBox = new JComboBox();
    instrumentTypeComboBox.setEditable(false);
    instrumentCreationPanel.add(instrumentTypeComboBox, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    connectionTypeLabel = new JLabel();
    connectionTypeLabel.setText("Connection Type");
    instrumentCreationPanel.add(connectionTypeLabel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    serverRadioButton = new JRadioButton();
    serverRadioButton.setSelected(true);
    serverRadioButton.setText("Server");
    instrumentCreationPanel.add(serverRadioButton, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    clientRadioButton = new JRadioButton();
    clientRadioButton.setText("Client");
    instrumentCreationPanel.add(clientRadioButton, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    hostnameLabel = new JLabel();
    hostnameLabel.setText("Hostname");
    instrumentCreationPanel.add(hostnameLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    hostnameTextField = new JTextField();
    hostnameTextField.setEditable(false);
    hostnameTextField.setEnabled(true);
    hostnameTextField.setText("localhost");
    instrumentCreationPanel.add(hostnameTextField, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    portLabel = new JLabel();
    portLabel.setText("Port Number");
    instrumentCreationPanel.add(portLabel, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    portTextField = new JTextField();
    portTextField.setText("10001");
    instrumentCreationPanel.add(portTextField, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    createInstrumentButton = new JButton();
    createInstrumentButton.setText("Create Instrument");
    instrumentCreationPanel.add(createInstrumentButton, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    consolePanel = new JScrollPane();
    contentPane.add(consolePanel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(800, 200), null, 0, false));
    consolePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Console", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    consoleTextArea = new JTextArea();
    consoleTextArea.setEditable(false);
    consoleTextArea.setLineWrap(true);
    consoleTextArea.setName("Console");
    consoleTextArea.setRows(1);
    consoleTextArea.setWrapStyleWord(true);
    consolePanel.setViewportView(consoleTextArea);
    instrumentsPanel = new JScrollPane();
    contentPane.add(instrumentsPanel, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 100), null, 0, false));
    instrumentsPanel.setBorder(BorderFactory.createTitledBorder(null, "Connected Instruments", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    instrumentsTable = new JTable();
    instrumentsTable.setEnabled(true);
    instrumentsTable.setRowSelectionAllowed(false);
    instrumentsTable.setUpdateSelectionOnSort(false);
    instrumentsPanel.setViewportView(instrumentsTable);
    reservedPanel = new JPanel();
    reservedPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    contentPane.add(reservedPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    reservedPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Future Use", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return contentPane;
  }
}
