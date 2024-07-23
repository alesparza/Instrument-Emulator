package com.github.alesparza.emulator.gui;

import com.github.alesparza.emulator.assay.Assay;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ResultPanel {
  private JPanel resultPanel;
  private JTable assayTable;
  private JScrollPane assayTableScrollPane;
  private JPanel topPanel;
  private JButton updateButton;
  private JButton newAssayButton;
  private JTextField testNameTextField;
  private JTextField testCodeTextField;
  private JTextField resultTextField;
  private JTextField unitsTextField;
  private JTextField completedDateTimeTextField;
  private JButton changeAssayButton;
  private JPanel buttonPannel;
  private JPanel currentAssayPanel;
  private JTextField currentAssayTextField;
  private JLabel currentAssayLabel;
  private JPanel indexPanel;
  private JLabel testCodeLabel;
  private JLabel testNameLabel;
  private JLabel resultlabel;
  private JLabel unitsLabel;
  private JLabel completedDTLabel;
  private JButton deleteThisAssayButton;
  private TableModel assayTableModel;
  ArrayList<Assay> assayArrayList;
  private JTextArea consoleTextArea;

  public ResultPanel() {
    // TODO: need to include the console log somehow
    assayArrayList = new ArrayList<>();

    assayTableModel = new AbstractTableModel() {
      private final String[] columnNames = {"Index", "Test Name","Test Code", "Result", "Units", "Completed Date/Time", "Comments"};
      @Override
      public int getRowCount() {
        return assayArrayList.size();
      }

      @Override
      public int getColumnCount() {
        return columnNames.length;
      }

      @Override
      public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
          case 0:
            return rowIndex;
          case 1:
            return assayArrayList.get(rowIndex).getName();
          case 2:
            return assayArrayList.get(rowIndex).getCode();
          case 3:
            return assayArrayList.get(rowIndex).getResult();
          case 4:
            return assayArrayList.get(rowIndex).getUnits();
          case 5:
            return assayArrayList.get(rowIndex).getCompleteDateTime();
          case 6:
            return assayArrayList.get(rowIndex).getPrintableComments();
        }
        return "";
      }

      @Override
      public String getColumnName(int column) {
        return columnNames[column];
      }
    };
    assayArrayList.add(new Assay("White Blood Cells", "WBC", "7.0"));
    assayArrayList.get(0).setCompleteDate("20240101120000");
    assayArrayList.get(0).setUnits("10*3\\uL");
    assayTable.setModel(assayTableModel);
    assayTable.repaint();

    // New Assay Button
    newAssayButton.addActionListener(new ActionListener() {
      /**
       * Add the contents of the fields as a new assay.
       */
      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO: check if Test Code already exists
        // TODO: check if fields are not empty and not too long
        // TODO: check if date/time is correct format
        int nextIndex = assayArrayList.size();
        Assay assay = new Assay(testNameTextField.getText(), testCodeTextField.getText(), resultTextField.getText());
        assay.setUnits(unitsTextField.getText());
        assay.setCompleteDate(completedDateTimeTextField.getText());
        assayArrayList.add(assay);
        assayTable.updateUI();
      }
    });

    // Update Assay Button
    updateButton.addActionListener(new ActionListener() {
      /**
       * Update the table based on the current contents displayed.
       */
      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO: check if Test Code already exists
        // TODO: check if fields are not empty and not too long
        // TODO: check if date/time is correct format
        // TODO: check index for out of range
        int index = Integer.parseInt(currentAssayTextField.getText());
        Assay assay = assayArrayList.get(index);
        assay.setName(testNameTextField.getText());
        assay.setCode(testCodeTextField.getText());
        assay.setResult(resultTextField.getText());
        assay.setUnits(unitsTextField.getText());
        assay.setCompleteDate(completedDateTimeTextField.getText());
        assayTable.updateUI();
      }
    });

    // Delete Assay Button
    deleteThisAssayButton.addActionListener(new ActionListener() {
      /**
       * Delete the assay in the index.
       */
      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO: check index for out of range
        int index = Integer.parseInt(currentAssayTextField.getText());
        assayArrayList.remove(index);
        testNameTextField.setText("");
        testCodeTextField.setText("");
        resultTextField.setText("");
        unitsTextField.setText("");
        completedDateTimeTextField.setText("");
        assayTable.updateUI();
        consoleTextArea.append("Delete test");
      }
    });


    // Change Loaded Assay Button
    changeAssayButton.addActionListener(new ActionListener() {
      /**
       * Change the printed text based on the index.
       */
      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO: check index for out of range
        int index = Integer.parseInt(currentAssayTextField.getText());
        Assay assay = assayArrayList.get(index);
        testNameTextField.setText(assay.getName());
        testCodeTextField.setText(assay.getCode());
        resultTextField.setText(assay.getResult());
        unitsTextField.setText(assay.getUnits());
        completedDateTimeTextField.setText(assay.getCompleteDateTime());
        assayTable.updateUI();
      }
    });



    //TODO: need some kind of "lock" button to prevent data from changing by accident.
    //TODO: either modified the index or modified the contents.  But not both at the same time
  }

  /**
   * Sets GUI Components
   * @param consoleTextArea
   */
  public void setGUIComponents(JTextArea consoleTextArea) {
    this.consoleTextArea = consoleTextArea;
  }
}
