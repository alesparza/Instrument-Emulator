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

  public String getName() {
    return nameTextField.getText();
  }

  public String getAddress() {
    return addressTextField.getText();
  }

  public String getPhone() {
    return phoneTextField.getText();
  }
}
