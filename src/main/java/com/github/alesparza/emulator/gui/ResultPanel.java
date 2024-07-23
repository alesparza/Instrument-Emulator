package com.github.alesparza.emulator.gui;

import com.github.alesparza.emulator.assay.Assay;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;

public class ResultPanel {
  private JPanel resultPanel;
  private JTable assayTable;
  private JScrollPane assayScrollPane;
  private JPanel topPanel;
  private JButton newAssayButton;
  private TableModel assayTableModel;
  ArrayList<Assay> assayArrayList;

  public ResultPanel() {
    assayArrayList = new ArrayList<>();

    assayTableModel = new AbstractTableModel() {
      private final String[] columnNames = {"Test Name","Test Code", "Result", "Units", "Completed Date/Time", "Comments"};
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
            return assayArrayList.get(rowIndex).getName();
          case 1:
            return assayArrayList.get(rowIndex).getCode();
          case 2:
            return assayArrayList.get(rowIndex).getResult();
            case 3:
              return assayArrayList.get(rowIndex).getUnits();
              case 4:
                return assayArrayList.get(rowIndex).getCompleteDateTime();
          case 5:
            return assayArrayList.get(rowIndex).getPrintableComments();
        }
        return "";
      }

      @Override
      public String getColumnName(int column) {
        return columnNames[column];
      }
    };
    assayTable.setModel(assayTableModel);
    assayTable.repaint();
  }

}
