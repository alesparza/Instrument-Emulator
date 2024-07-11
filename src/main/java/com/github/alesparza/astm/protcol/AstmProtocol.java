package com.github.alesparza.astm.protcol;

import com.github.alesparza.ascii.Ascii;
import com.github.alesparza.astm.component.Record;

/**
 * Utilities to construct ASTM messages.
 */
public class AstmProtocol {

  /**
   * Invalid frame number: -1
   */
  public static int INVALID_FRAME = -1;

  /**
   * Modulo used in checksum calculation: 256
   */
  public static int CHECKSUM_MODULO = 256;

  /**
   * Modulo used in frame cycle: 8
   */
  public static int FRAME_MODULO = 8;

  /**
   * Calculates the checksum for ASTM message.
   * <br>
   * The checksum is calculated by adding the binary value of each character,
   * keeping only the eight least significant bits (i.e. mod 256).
   * The checksum initialises to 0 and does not include STX.  It does include the frame number and ETX or EOB.
   * The checksum characters are converted to the hexadecimal representation.
   * Example:
   * <STX>1H|\!~|||DxH|||||LIS||P|LIS2-A|20240703125902<CR><ETX>59<CR><LF>
   * @param data the message from STX through the end of data and the ETX or ETB.
   * @return the checksum of the message as two ASCII characters of the hexadecimal value.
   */
  public static byte[] calculateChecksum(byte[] data) {
    int sum = 0;
    // start at i = 1 to skip STX
    for (int i = 1; i < data.length; i++) {
      sum = (sum + data[i]) % CHECKSUM_MODULO;
    }
    return Integer.toHexString(sum).getBytes();
  }

  /**
   * Generates an ASTM message based on the data, frame number, and if there is more to send
   * @param frame the frame number for this message
   * @param data the data to send in the message
   * @param isLast if this data is the last message for this record
   * @return the ASTM message for sending
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

    byte[] checksum = calculateChecksum(toSend);
    toSend[data.length + 3] = checksum[0]; // checksum byte 1
    toSend[data.length + 4] = checksum[1]; // checksum byte 2
    toSend[data.length + 5] = Ascii.CntlChar.CR.getAsciiByte(); // CR
    toSend[data.length + 6] = Ascii.CntlChar.LF.getAsciiByte(); // LF
    return toSend;
  }

  /**
   * Generates an H record.
   * <br>
   * Example: <STX>1H|\!~|||DxH|||||LIS||P|LIS2-A|20240703125902<CR><ETX>59<CR><LF>
   * @param astmConfiguration the ASTM configuration to use for this record
   * @return
   */
  public static byte[] generateHRecord(AstmConfiguration astmConfiguration, Record record) {
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
    //sb.append("TeleNum");
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
    sb.append("20240703125902");

    // end of record
    sb.append("\r");

    return sb.toString().getBytes();
  }
}
