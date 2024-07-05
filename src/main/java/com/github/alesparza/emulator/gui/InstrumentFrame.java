package com.github.alesparza.emulator.gui;

import javax.swing.*;

public class InstrumentFrame extends JFrame {
  private JPanel contentPanel;
  private JLabel topLabel;
  private JTabbedPane tabbedPane1;
  private JPanel commandsPanel;
  private JScrollPane consoleScrollablePane;
  private JTextArea consoleTextArea;
  private JTextArea commTextArea;
  private JScrollPane commScrollablePane;

  public InstrumentFrame() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setContentPane(contentPanel);




    pack();
  }
}
