package com.github.alesparza.emulator.processor;

import com.github.alesparza.emulator.gui.GUI;

import javax.swing.*;

public class Emulator {
  public static void main(String[] args) {
    GUI gui = new GUI();

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        gui.setVisible(true);
      }
    });
  }
}