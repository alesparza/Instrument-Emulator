package com.github.alesparza.emulator.gui;

import com.github.alesparza.emulator.assay.Assay;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
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

  public ResultPanel() {
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
    assayArrayList.add(new Assay("Red Blood Cells", "RBC", "4.0"));
    assayArrayList.get(1).setCompleteDate("20240101120000");
    assayArrayList.get(1).setUnits("10*3\\uL");
    assayArrayList.add(new Assay("Hemoglobin", "HGB", "15.0"));
    assayArrayList.get(2).setCompleteDate("20240101120000");
    assayArrayList.get(2).setUnits("g\\dL");
    assayArrayList.add(new Assay("Hematocrit", "HCT", "45"));
    assayArrayList.get(3).setCompleteDate("20240101120000");
    assayArrayList.get(3).setUnits("%");
    assayTable.setModel(assayTableModel);
    assayTable.repaint();
  }

}
