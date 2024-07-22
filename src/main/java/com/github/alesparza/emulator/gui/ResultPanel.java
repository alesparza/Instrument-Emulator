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
  private TableModel assayTableModel;
  ArrayList<Assay> assayArrayList;

  public ResultPanel() {
    assayArrayList = new ArrayList<>();

    assayTableModel = new AbstractTableModel() {
      private final String[] columnNames = {"Test Name","Test Code", "Result", "Comments"};
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
            return assayArrayList.get(rowIndex).getPrintableComments();
        }
        return "";
      }

      @Override
      public String getColumnName(int column) {
        return columnNames[column];
      }
    };
    assayArrayList.add(new Assay("White Blood Cells", "WBC", "7"));
    assayArrayList.get(0).addComment("Test 1");
    assayArrayList.get(0).addComment("Test 2");
    assayTable.setModel(assayTableModel);
    assayTable.repaint();
  }

}
