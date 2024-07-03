package com.github.alesparza.emulator.processor;

import com.github.alesparza.emulator.gui.StartFrame;

import javax.swing.*;

public class Emulator {
  public static void main(String[] args) {
    StartFrame startFrame = new StartFrame();

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        startFrame.setVisible(true);
      }
    });
  }
}