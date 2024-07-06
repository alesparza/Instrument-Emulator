package com.github.alesparza.astm;

/**
 * Formats ASCII Strings into a human-readable format.
 */
public class Ascii {

  /**
   * Enum for different control characters.
   */
  public enum CntlChar {
    NUL((byte) 0x00, "\0", "<NUL>"),
    SOH((byte) 0x01, "\1", "<SOH>"),
    STX((byte) 0x02, "\2", "<STX>"),
    ETX((byte) 0x03, "\3", "<ETX>"),
    EOT((byte) 0x04, "\4", "<EOT>"),
    ENQ((byte) 0x05, "\5" ,"<ENQ>"),
    ACK((byte) 0x06, "\6", "<ACK>"),
    TAB((byte) 0x09, "\t", "<TAB>"),
    LF((byte) 0x0a, "\n", "<LF>"),
    VT((byte) 0x0b, "\13", "<VT>"),
    CR((byte) 0x0d, "\r", "<CR>"),
    NAK((byte) 0x15, "\25","<NAK>"),
    ;

    /**
     * Byte value of control character.
     */
    private final byte asciiByte;

    /**
     * Escape string for control character.
     */
    private final String asciiEscape;

    /**
     * Formatted string for control character.
     */
    private final String asciiFormat;

    public final byte getAsciiByte() {
      return this.asciiByte;
    }

    CntlChar(byte b, String e, String s) {
      asciiByte = b;
      asciiEscape = e;
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
    for (CntlChar format : CntlChar.values()) {
      ret = ret.replaceAll(format.asciiEscape, format.asciiFormat);
    }
    return ret;
  }
}
