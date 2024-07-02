package com.github.alesparza.emulator.gui;

import javax.swing.*;

public class GUI extends JFrame {
  private JPanel contentPane;
  private JTextField hostnameTextField;
  private JTextField portTextField;
  private JLabel hostnameLabel;
  private JLabel portLabel;
  private JLabel instrumentModeLabel;
  private JLabel titleLabel;
  private JButton connectButton;
  private JButton quitButton;
  private JComboBox comboBox1;

  public GUI () {
    setTitle("Emulator");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setContentPane(contentPane);
    pack();
  }
}
