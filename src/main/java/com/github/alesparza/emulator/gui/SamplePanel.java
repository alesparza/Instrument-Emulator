package com.github.alesparza.emulator.gui;

import javax.swing.*;
import java.awt.*;

public class SamplePanel {
  private JPanel samplePanel;
  private JTextField specimenIDTextField;
  private JTextField positionIDTextField;
  private JTextField priorityTextField; // TODO: make it a combobox?
  private JTextField requestDateAndTimeTextField;
  private JTextField collectionDateAndTimeTextField;
  private JTextField receivedDateAndTimeTextField;
  private JTextField specimenTypeTextField;
  private JTextField releasedDateAndTimeTextField;
  private JLabel positionIDLabel;
  private JLabel specimenIDLabel;
  private JLabel priorityLabel;
  private JLabel requestDTLabel;
  private JLabel collectDTLabel;
  private JLabel receiveDTLabel;
  private JLabel completeDTLabel;
  private JLabel specimenTypeLabel;

  public String getReleasedDateAndTime() {
    return releasedDateAndTimeTextField.getText();
  }

  public String getSpecimenType() {
    return specimenTypeTextField.getText();
  }

  public String getReceivedDateAndTime() {
    return receivedDateAndTimeTextField.getText();
  }

  public String getCollectionDateAndTime() {
    return collectionDateAndTimeTextField.getText();
  }

  public String getRequestDateAndTime() {
    return requestDateAndTimeTextField.getText();
  }

  public String getPriority() {
    return priorityTextField.getText();
  }

  public String getPositionID() {
    return positionIDTextField.getText();
  }

  public String getSpecimenID() {
    return specimenIDTextField.getText();
  }
}
