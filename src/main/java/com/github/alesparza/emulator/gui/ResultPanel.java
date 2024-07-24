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
  private JPanel indexPanel;
  private JLabel testCodeLabel;
  private JLabel testNameLabel;
  private JLabel resultlabel;
  private JLabel unitsLabel;
  private JLabel completedDTLabel;
  private JButton deleteAssayButton;
  private JCheckBox lockCheckBox;
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
        // TODO: check if fields are not empty and not too long
        // TODO: check if date/time is correct format

        // check if the record is locked.  only update when unlocked
        if (lockCheckBox.isSelected()) {
          consoleTextArea.append("Assay record locked.  Unlock before creating a new assay\n");
          return;
        }

        if (!isValidAssay()) {
          consoleTextArea.append("Not a valid assay configuration, no new assay created\n");
          return;
        }

        String name = testNameTextField.getText();
        String code = testCodeTextField.getText();

        // check for duplicate names and codes, these must be unique
        for (Assay assay : assayArrayList) {
          if (name.equals(assay.getName())) {
            consoleTextArea.append("Test name already exists: " + name + "\n");
            return;
          }
          if (code.equals(assay.getCode())) {
            consoleTextArea.append("Test code already exists: " + code + "\n");
            return;
          }
        }

        // go ahead and make the new assay
        int nextIndex = assayArrayList.size();
        Assay assay = createAssay();
        assayArrayList.add(assay);
        assayTable.updateUI();
        consoleTextArea.append("Created new assay: " + name + "\n");
        currentAssayTextField.setText(String.valueOf(nextIndex));
        lock();
        loadAssay(nextIndex);
      }
    });

    // Update Assay Button
    updateButton.addActionListener(new ActionListener() {
      /**
       * Update the table based on the current contents displayed.
       */
      @Override
      public void actionPerformed(ActionEvent e) {
        // only update the assay when the index is locked
        if (lockCheckBox.isSelected()) {
          consoleTextArea.append("Assay record locked.  Unlocked before update contents.\n");
          return;
        }

        // still check if the assay is in the right format
        if (!isValidAssay()) {
          consoleTextArea.append("Not a valid assay configuration, not updated\n");
          return;
        }

        // replace the current (valid) index with the current display contents
        try {
          int index = Integer.parseInt(currentAssayTextField.getText());
          Assay assay = createAssay();
          assayArrayList.set(index, assay);
          updateAssayDisplay(assay);
          consoleTextArea.append("Updated assay at index " + index + ".\n");
        } catch (NumberFormatException ex) {
          consoleTextArea.append("No assay at index " + currentAssayTextField.getText() + ".\n");
        }
      }
    });

    // Delete Assay Button
    deleteAssayButton.addActionListener(new ActionListener() {
      /**
       * Delete the assay in the index.
       */
      @Override
      public void actionPerformed(ActionEvent e) {
        // ensure the assay record is unlocked (locking the assay index)
        if (!lockCheckBox.isSelected()) {
          consoleTextArea.append("Assay record not locked.\n");
          return;
        }

        // check if locked index is out of range
        int index = -1;
        try {
          index = Integer.parseInt(currentAssayTextField.getText());
        } catch (NumberFormatException ex) {
          consoleTextArea.append("No assay at index " + currentAssayTextField.getText() + ".\n");
          return;
        }

        // check if the name is the same.  if not, update the display only
        String name = assayArrayList.get(index).getName();
        if (!name.equals(testNameTextField.getText())) {
          consoleTextArea.append("Index " + index + " does not contain " + name + ".  Updating display...\n");
          updateAssayDisplay(assayArrayList.get(index));
          consoleTextArea.append("Click delete button again to confirm.\n");
          return;
        }

        // reaching here, all checks pass, go ahead and remove it
        assayArrayList.remove(index);
        clearAssay();
        consoleTextArea.append("Deleted assay " + name + " at index " + index + ".\n");
      }
    });

    // Change Loaded Assay Button
    changeAssayButton.addActionListener(new ActionListener() {
      /**
       * Change the printed text based on the index.
       */
      @Override
      public void actionPerformed(ActionEvent e) {
        // only change the display when the record is locked (to unlock the index).
        if (!lockCheckBox.isSelected()) {
          consoleTextArea.append("Index not unlocked.  Unlock to select the index.\n");
          return;
        }

        // only change display for valid index
        try {
          int index = Integer.parseInt(currentAssayTextField.getText());
          Assay assay = assayArrayList.get(index);
          updateAssayDisplay(assay);
        } catch (NumberFormatException ex) {
          consoleTextArea.append("No assay at index " + currentAssayTextField.getText() + ".\n");
        }
      }
    });

    lockCheckBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (lockCheckBox.isSelected()) {
          lock();
        }
        else {
          unlock();
        }
      }
    });
  }

  /**
   * Loads an assay into the fields.
   * @param index index in table
   */
  public void loadAssay(String index) {
    try {
      int intIndex = Integer.parseInt(index);
      loadAssay(intIndex);
    } catch (NumberFormatException ex) {
      consoleTextArea.append("Index must be numeric");
    }
  }

  /**
   * Loads an assay into the fields.
   * @param index index in table
   */
  public void loadAssay(int index) {
    try {
    Assay assay = assayArrayList.get(index);
    updateAssayDisplay(assay);
    consoleTextArea.append("Loaded assay " + index + ": " + assay.getName() + "\n");
    } catch (IndexOutOfBoundsException e) {
      consoleTextArea.append("Assay index " + index + " does not exist\n");
    }
  }

  /**
   * Locks the results pages from editing.  Only the index can be changed.
   */
  public void lock() {
    lockCheckBox.setSelected(true);
    lockCheckBox.setText("Locked");
    currentAssayTextField.setEditable(true);
    testNameTextField.setEditable(false);
    testCodeTextField.setEditable(false);
    resultTextField.setEditable(false);
    unitsTextField.setEditable(false);
    completedDateTimeTextField.setEditable(false);
  }

  /**
   * Locks the results pages from editing.  Only the index can be changed.
   */
  public void unlock() {
    lockCheckBox.setSelected(false);
    lockCheckBox.setText("Unlocked");
    currentAssayTextField.setEditable(false);
    testNameTextField.setEditable(true);
    testCodeTextField.setEditable(true);
    resultTextField.setEditable(true);
    unitsTextField.setEditable(true);
    completedDateTimeTextField.setEditable(true);
  }

  /**
   * Updates the display with a specific assay
   * @param assay
   */
  public void updateAssayDisplay(Assay assay) {
    testNameTextField.setText(assay.getName());
    testCodeTextField.setText(assay.getCode());
    resultTextField.setText(assay.getResult());
    unitsTextField.setText(assay.getUnits());
    completedDateTimeTextField.setText(assay.getCompleteDateTime());
    assayTable.updateUI();
  }

  /**
   * Clears the displayed assay.  Does not delete the assay from the list of assays.
   */
  public void clearAssay() {
    testNameTextField.setText("");
    testCodeTextField.setText("");
    resultTextField.setText("");
    unitsTextField.setText("");
    completedDateTimeTextField.setText("");
    assayTable.updateUI();
  }

  /**
   * Creates an Assay based on the current field contents.
   * @return new Assay
   */
  public Assay createAssay() {
    Assay assay = new Assay(testNameTextField.getText(), testCodeTextField.getText(), resultTextField.getText());
    assay.setUnits(unitsTextField.getText());
    assay.setCompleteDate(completedDateTimeTextField.getText());
    return assay;
  }

  /**
   * Checks if the current display is valid or not
   * @return true if the assay is valid, false otherwise.
   */
  public boolean isValidAssay() {
    // check for empty entries.
    String testName = testNameTextField.getText();
    if (testName.isEmpty()) {
      consoleTextArea.append("Test name cannot be blank.\n");
      return false;
    }
    String testCode = testCodeTextField.getText();
    if (testCode.isEmpty()) {
      consoleTextArea.append("Test code cannot be blank.\n");
      return false;
    }
    String result = resultTextField.getText();
    if (result.isEmpty()) {
      consoleTextArea.append("Result cannot be blank.\n");
      return false;
    }
    String units = unitsTextField.getText();
    if (units.isEmpty()) {
      consoleTextArea.append("Units cannot be blank.\n");
      return false;
    }
    String dateTime = completedDateTimeTextField.getText();
    if (dateTime.isEmpty()) {
      consoleTextArea.append("Completed Date/Time cannot be blank.\n");
      return false;
    }

    // check for date/time format
    if (dateTime.length() != 14) {
      consoleTextArea.append("Date and Time must be YYYYMMDDHHMMSS format.\n");
      return false;
    }

    try {
      Double check = Double.parseDouble(dateTime);
    } catch (NumberFormatException ex) {
      consoleTextArea.append("Date and Time must be YYYYMMDDHHMMSS format.\n");
      return false;
    }

    return true;
  }

  /**
   * Sets GUI Components
   * @param consoleTextArea
   */
  public void setGUIComponents(JTextArea consoleTextArea) {
    this.consoleTextArea = consoleTextArea;
  }
}
