package com.github.alesparza.emulator.gui;

import com.github.alesparza.emulator.assay.Assay;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
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
      private final String[] columnNames = {"Index", "Test Name", "Test Code", "Result", "Units", "Completed Date/Time", "Comments"};

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
          consoleTextArea.append("Index not a number: " + currentAssayTextField.getText() + ".\n");
        } catch (ArrayIndexOutOfBoundsException ex) {
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
        } catch (NumberFormatException ex) {
          consoleTextArea.append("Not a number: " + currentAssayTextField.getText() + ".\n");
          return;
        } catch (IndexOutOfBoundsException ex) {
          consoleTextArea.append("No assay at index " + currentAssayTextField.getText() + ".\n");
        }
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
        } catch (IndexOutOfBoundsException ex) {
          consoleTextArea.append("No assay at index " + currentAssayTextField.getText() + ".\n");
        } catch (NumberFormatException ex) {
          consoleTextArea.append("Not a number: " + currentAssayTextField.getText() + ".\n");
        }
      }
    });

    lockCheckBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (lockCheckBox.isSelected()) {
          lock();
        } else {
          unlock();
        }
      }
    });
  }

  /**
   * Loads an assay into the fields.
   *
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
   *
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
   *
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
   *
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
   *
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
   *
   * @param consoleTextArea
   */
  public void setGUIComponents(JTextArea consoleTextArea) {
    this.consoleTextArea = consoleTextArea;
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
    resultPanel = new JPanel();
    resultPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
    assayTableScrollPane = new JScrollPane();
    assayTableScrollPane.setVerticalScrollBarPolicy(20);
    resultPanel.add(assayTableScrollPane, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    assayTable = new JTable();
    assayTable.setPreferredScrollableViewportSize(new Dimension(150, 400));
    assayTableScrollPane.setViewportView(assayTable);
    topPanel = new JPanel();
    topPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
    resultPanel.add(topPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    currentAssayPanel = new JPanel();
    currentAssayPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), -1, -1));
    topPanel.add(currentAssayPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    testNameLabel = new JLabel();
    testNameLabel.setText("Test Name");
    currentAssayPanel.add(testNameLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
    currentAssayPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 5, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    testNameTextField = new JTextField();
    testNameTextField.setToolTipText("Name of the test");
    currentAssayPanel.add(testNameTextField, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
    testCodeLabel = new JLabel();
    testCodeLabel.setText("Test Code");
    currentAssayPanel.add(testCodeLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    testCodeTextField = new JTextField();
    testCodeTextField.setToolTipText("Interface test code/alias");
    currentAssayPanel.add(testCodeTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
    resultlabel = new JLabel();
    resultlabel.setText("Result");
    currentAssayPanel.add(resultlabel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    resultTextField = new JTextField();
    resultTextField.setToolTipText("Result of test");
    currentAssayPanel.add(resultTextField, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
    unitsLabel = new JLabel();
    unitsLabel.setText("Units");
    currentAssayPanel.add(unitsLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    unitsTextField = new JTextField();
    unitsTextField.setToolTipText("Units of test");
    currentAssayPanel.add(unitsTextField, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
    completedDTLabel = new JLabel();
    completedDTLabel.setText("Completed Date/Time");
    currentAssayPanel.add(completedDTLabel, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    completedDateTimeTextField = new JTextField();
    completedDateTimeTextField.setToolTipText("Must be YYYYMMDDHHMMSS format");
    currentAssayPanel.add(completedDateTimeTextField, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
    buttonPannel = new JPanel();
    buttonPannel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
    topPanel.add(buttonPannel, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    updateButton = new JButton();
    updateButton.setText("Update This Assay");
    updateButton.setToolTipText("Update the index with the current contents");
    buttonPannel.add(updateButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    newAssayButton = new JButton();
    newAssayButton.setText("Add As New Assay");
    newAssayButton.setToolTipText("Add contents as new assay");
    buttonPannel.add(newAssayButton, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    changeAssayButton = new JButton();
    changeAssayButton.setText("Change Loaded Assay");
    changeAssayButton.setToolTipText("Change contents to the entered index");
    buttonPannel.add(changeAssayButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    deleteAssayButton = new JButton();
    deleteAssayButton.setText("Delete This Assay");
    deleteAssayButton.setToolTipText("Delete the current index");
    buttonPannel.add(deleteAssayButton, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    indexPanel = new JPanel();
    indexPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
    topPanel.add(indexPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(100, -1), null, 0, false));
    indexPanel.setBorder(BorderFactory.createTitledBorder(null, "Current Assay", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    currentAssayTextField = new JTextField();
    currentAssayTextField.setEditable(false);
    currentAssayTextField.setText("0");
    currentAssayTextField.setToolTipText("Which row is currently displayed");
    indexPanel.add(currentAssayTextField, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
    lockCheckBox = new JCheckBox();
    lockCheckBox.setText("Unlocked");
    indexPanel.add(lockCheckBox, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
    topPanel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    testNameLabel.setLabelFor(testNameTextField);
    testCodeLabel.setLabelFor(testCodeTextField);
    resultlabel.setLabelFor(resultTextField);
    unitsLabel.setLabelFor(unitsTextField);
    completedDTLabel.setLabelFor(completedDateTimeTextField);
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return resultPanel;
  }
}
