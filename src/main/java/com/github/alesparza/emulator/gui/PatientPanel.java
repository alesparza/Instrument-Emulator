package com.github.alesparza.emulator.gui;

import javax.swing.*;

public class PatientPanel {
  private JPanel mainPanel;
  private JTextField firstTextField;
  private JTextField mTextField;
  private JTextField lastTextField;
  private JTextField mrnTextField;
  private JTextField dobTextField;
  private JTextField sexTextField;
  private JLabel middleNameLabel;
  private JLabel firstNameLabel;
  private JLabel lastNameLabel;
  private JLabel patientIDLabel;
  private JLabel dobLabel;
  private JLabel sexLabel;
  private JTextField locationTextField;
  private JLabel locationLabel;


  public String getFirstName() {
    return firstTextField.getText();
  }

  public String getMiddleName() {
    return mTextField.getText();
  }

  public String getLastName() {
    return lastTextField.getText();
  }

  public String getPatientID() {
    return mrnTextField.getText();
  }

  public String getDOB() {
    return dobTextField.getText();
  }

  public String getSex() {
    return sexTextField.getText();
  }

  public String getLocation() {
    return locationTextField.getText();
  }

  // TODO: connect to console logger
  // TODO: make DOB a chooser, but only return the formatted one

}
