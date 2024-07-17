package com.github.alesparza.emulator.gui;

import javax.swing.*;

public class DevicePanel {
  private JPanel devicePanel;
  private JTextField nameTextField;
  private JTextField addressTextField;
  private JTextField phoneTextField;
  private JLabel nameLabel;
  private JLabel addressLabel;
  private JLabel phoneLabel;
  private JTextField typeTextField;
  private JLabel typeLabel;
  private JTextField deviceIDTextField;
  private JLabel deviceIDLabel;

  public String getName() {
    return nameTextField.getText();
  }

  public String getAddress() {
    return addressTextField.getText();
  }

  public String getPhone() {
    return phoneTextField.getText();
  }

  public JTextField getTypeTextField() {
    return this.typeTextField;
  }

  public String getDeviceID() {
    return deviceIDTextField.getText();
  }
}
