package com.github.alesparza.astm.protcol;

import com.github.alesparza.ascii.Ascii;

/**
 * Utilities to construct ASTM messages.
 */
public class AstmProtocol {

  /**
   * Generates an ASTM message based on the data, frame number, and if there is more to send
   * @param frame
   * @param data
   * @return
   */
  public static byte[] generateMessage(int frame, byte[] data, boolean isLast) {
    // data + STX, frame#, ETX, checksum a/b, CR, LF
    byte[] toSend = new byte[data.length + 7];
    toSend[0] = Ascii.CntlChar.STX.getAsciiByte();
    toSend[1] = Integer.toString(frame).getBytes()[0];
    System.arraycopy(data, 0, toSend, 2, data.length);
    if (isLast) {
      toSend[data.length + 2] = Ascii.CntlChar.ETX.getAsciiByte(); // ETX
    }
    else {
      toSend[data.length + 2] = Ascii.CntlChar.ETB.getAsciiByte(); // ETB
    }

    //TODO: calculate checksum
    toSend[data.length + 3] = (byte) 'x'; // checksum byte 1
    toSend[data.length + 4] = (byte) 'x'; // checksum byte 2
    toSend[data.length + 5] = Ascii.CntlChar.CR.getAsciiByte(); // CR
    toSend[data.length + 6] = Ascii.CntlChar.LF.getAsciiByte(); // LF
    return toSend;
  }

  public static byte[] generateHRecord(AstmConfiguration astmConfiguration) {
    StringBuilder sb = new StringBuilder();
    // field 1: Record Identifier
    sb.append("H");

    // field 2: Delimiters
    sb.append(astmConfiguration.getFieldDelimiter());
    sb.append(astmConfiguration.getRepeatDelimiter());
    sb.append(astmConfiguration.getComponentDelimiter());
    sb.append(astmConfiguration.getEscapeDelimiter());
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 3: Message Control ID
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 4: Access password
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 5: Sender Name
    sb.append("DxH");
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 6: Sender Street Address
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 7: Reserved
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 8: Sender Telephone Number
    sb.append("TeleNum");
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 9: Characteristics of Sender
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 10: Receiver ID
    sb.append("LIS");
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 11: comment or special instructions
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 12: Processing ID
    sb.append("P");
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 13: Version Number
    sb.append("LIS2-A");
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 14: Date and Time of Message
    sb.append("20240711123500");

    // end of record
    sb.append("\r");

    return sb.toString().getBytes();
  }
}
