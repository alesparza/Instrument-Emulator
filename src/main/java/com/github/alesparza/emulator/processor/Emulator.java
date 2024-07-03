package com.github.alesparza.emulator.processor;

import com.github.alesparza.emulator.gui.ConnectionBuildPanel;
import com.github.alesparza.emulator.gui.StartPanel;

import javax.swing.*;

public class Emulator {
  public static void main(String[] args) {
    //ConnectionBuildPanel connectionBuildPanel = new ConnectionBuildPanel();
    StartPanel startPanel = new StartPanel();

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        //connectionBuildPanel.setVisible(true);
        startPanel.setVisible(true);
      }
    });
  }
}