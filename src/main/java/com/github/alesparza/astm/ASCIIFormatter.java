package com.github.alesparza.astm;

public class ASCIIFormatter {

public enum AsciiString {
  NUL((byte) 0x00, "<NUL>"),
  SOH((byte) 0x01, "<SOH>"),
  STX((byte) 0x02, "<STX>"),
  ETX((byte) 0x03, "<ETX>"),
  EOT((byte) 0x04, "<EOT>"),
  ENQ((byte) 0x05, "<ENQ>"),
  ACK((byte) 0x06, "<ACK>"),
  TAB((byte) 0x09, "<TAB>"),
  LF((byte) 0x0a, "<LF>"),
  VT((byte) 0x0b, "<VT>"),
  CR((byte) 0x0d, "<CR>"),
  NAK((byte) 0x15, "<NAK>"),
  ;

  private final byte asciiByte;
  private final String asciiFormat;

  private byte getAsciiByte() {
    return asciiByte;
  }

  private String getAsciiFormat() {
    return asciiFormat;
  }

  AsciiString(byte b, String s) {
    asciiByte = b;
    asciiFormat = s;
  }
}


  /**
   * Formats a byte array to a readable String.
   * @param bytes the bytes to format
   * @return a readable string
   */
  public static String getFormattedString(byte[] bytes) {
    String ret = new String(bytes);
    for (AsciiString format : AsciiString.values()) {
      ret = ret.replaceAll(String.valueOf(format.asciiByte), format.asciiFormat);
    }
    return ret;
  }

}
