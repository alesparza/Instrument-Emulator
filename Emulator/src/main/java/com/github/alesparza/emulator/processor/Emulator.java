package com.github.alesparza.emulator.processor;

import com.github.alesparza.emulator.gui.GUI;

import javax.swing.*;
import java.nio.charset.StandardCharsets;

public class Emulator {
  public static void main(String[] args) {


    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        GUI gui = new GUI();
        gui.setVisible(true);
      }
    });

    // set up the emulator
    if (args.length != 2) {
      System.out.println("Usage: java Emulator hostname port");
    }
    String hostname = args[0];
    int port = Integer.parseInt(args[1]);

    Instrument instrument = new Instrument();
    instrument.initialiseSocket(hostname, port);

    // send an ENQ, wait for ACK, close with EOT
    instrument.sendEnq();

    byte[] response = instrument.receiveMessage();
    System.out.println(new String(response, StandardCharsets.US_ASCII));

    instrument.sendAck();

    System.out.println("Bye bye!");
  }
}