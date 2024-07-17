package com.github.alesparza.astm.protcol;

import com.github.alesparza.ascii.Ascii;
import com.github.alesparza.astm.component.Record;
import com.github.alesparza.astm.component.RecordType;

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
    if (sum < 15) {
      return ("0" + Integer.toHexString(sum)).toUpperCase().getBytes();
    }
    return Integer.toHexString(sum).toUpperCase().getBytes();
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
   * @param record
   * @return
   */
  public static byte[] generateHRecord(AstmConfiguration astmConfiguration, Record record) {
    StringBuilder sb = new StringBuilder();
    // field 1: Record Identifier
    sb.append(RecordType.H);

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
    sb.append(record.getField(5).getComponent(0).getData());
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 6: Sender Street Address
    sb.append(record.getField(6).getComponent(0).getData());
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 7: Reserved
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 8: Sender Telephone Number
    sb.append(record.getField(8).getComponent(0).getData());
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 9: Characteristics of Sender
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 10: Receiver ID
    sb.append(record.getField(10).getComponent(0).getData()); //TODO: from record/form but need to add textfield
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 11: comment or special instructions
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 12: Processing ID
    sb.append("P"); // pretty sure for emulator purposes, this is always P
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 13: Version Number
    sb.append("LIS2-A"); // hardcoded protocol version
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 14: Date and Time of Message
    sb.append("20240703125902"); //TODO: use Now() YYYYMMDDHHMMSS

    // end of record
    sb.append("\r");

    return sb.toString().getBytes();
  }

  /**
   * Generates a P record.
   * <br>
   * Example: <STX>2P|1||641647714||ZZZTSTTWO!HUPLAB||19731107!50!Y|F|||||||||||||||||HCDS HUP Centr<CR><ETX>C5<CR><LF>
   * @param astmConfiguration the ASTM configuration to use for this record
   * @param record
   * @return
   */
  public static byte[] generatePRecord(AstmConfiguration astmConfiguration, Record record) {
    StringBuilder sb = new StringBuilder();
    // field 1: Record Identifier
    sb.append(RecordType.P);
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 2: Sequence Number
    sb.append("1"); // TODO: will this ever be longer than one?
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 3: Practise Assigned Patient ID
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 4: Laboratory Assigned Patient ID
    sb.append(record.getField(4).getComponent(0).getData());
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 5: Patient ID Flag
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 6: Patient Name
    sb.append(record.getField(6).getComponent(0).getData()); // last name
    sb.append(astmConfiguration.getComponentDelimiter());
    sb.append(record.getField(6).getComponent(1).getData()); // first name
    sb.append(astmConfiguration.getComponentDelimiter());
    sb.append(record.getField(6).getComponent(2).getData()); // middle name
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 7: Mother's Maiden Name
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 8: Birthdate (YYYYMMDD)
    sb.append(record.getField(8).getComponent(0).getData());
    sb.append(astmConfiguration.getComponentDelimiter());
    // skip age
    sb.append(astmConfiguration.getComponentDelimiter());
    // skip age units
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 9: Sex
    sb.append(record.getField(9).getComponent(0).getData());
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 10: Patient Race-Ethnic Origin
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 11: Patient Address
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 12: Reserved
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 13: Patient Telephone
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 14: Attending Physician ID
    // skip ID
    sb.append(astmConfiguration.getComponentDelimiter());
    // skip last name
    sb.append(astmConfiguration.getComponentDelimiter());
    // skip first name
    sb.append(astmConfiguration.getComponentDelimiter());
    // skip middle name
    sb.append(astmConfiguration.getComponentDelimiter());
    // skip suffix
    sb.append(astmConfiguration.getComponentDelimiter());
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 15: Special Field 1
    // skip user field 1
    sb.append(astmConfiguration.getComponentDelimiter());
    // skip user field 2
    sb.append(astmConfiguration.getComponentDelimiter());
    // skip user field 3
    sb.append(astmConfiguration.getComponentDelimiter());
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 16: Special Field 2
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 17: Patient Height
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 18: Patient Weight
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 19: Patient Diagnosis
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 20: Patient Medications
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 21: Patient Diet
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 22: Practise Field #1
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 23: Practise Field #2
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 24: Admission and Discharge Dates
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 25: Admission Status
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 26: Patient Location
    sb.append(record.getField(26).getComponent(0).getData());
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 27: Nature of Alt. Diag. Code & Class
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 28: Alt. Diag. Code & Class
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 29: Patient Religion
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 30: Patient Marital Status
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 31: Isolation Status
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 32: Hospital Service
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 33: Hospital Institution
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 34: Dosage Category
    // skipped

    // end of record
    sb.append("\r");

    return sb.toString().getBytes();
  }

  /**
   * Generates an O record.
   * <br>
   * Example: <STX>3O|1|18590023A|00006|!!!CD|R|||||||||20240703125656|Whole blood|||||!SYSTEM||20240703125901|||F<CR><ETX>F8<CR><LF>
   * @param astmConfiguration the ASTM configuration to use for this record
   * @param record
   * @return
   */
  public static byte[] generateORecord(AstmConfiguration astmConfiguration, Record record) {
    StringBuilder sb = new StringBuilder();
    // field 1: Record Identifier
    sb.append(RecordType.O);
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 2: Sequence Number
    sb.append("1"); // TODO: will this ever be longer than one?
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 3: Specimen ID
    sb.append(record.getField(3).getComponent(0).getData());
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 4: Instrument Specimen ID
    sb.append(record.getField(4).getComponent(0).getData());
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 5: Universal Test ID
    // name: skipped
    sb.append(astmConfiguration.getComponentDelimiter());
    // type: skipped
    sb.append(astmConfiguration.getComponentDelimiter());
    sb.append(record.getField(5).getComponent(2).getData()); // Local Test Code: this is usually the only one used
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 6: Priority
    sb.append(record.getField(6).getComponent(0).getData());
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 7: Requested Date and Time
    sb.append(record.getField(7).getComponent(0).getData());
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 8: Collection Date and Time
    sb.append(record.getField(8).getComponent(0).getData());
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 9: Collection End Time
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 10: Collection Volume
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 11: Collector ID
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 12: Action Code
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 13: Danger Code
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 14: Relevant Clinical Info
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 15: Received Data and Time
    sb.append(record.getField(15).getComponent(0).getData());
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 16: Specimen Descriptor (specimen type)
    sb.append(record.getField(16).getComponent(0).getData());
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 17: Ordering Physician
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 18: Physician Phone Number
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 19: User Field #1
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 20 User Field #2
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 21: Laboratory Field #1
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 22: Laboratory Field #2
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 23: Release Date and Time
    sb.append(record.getField(23).getComponent(0).getData());
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 24: Instr. Change to Computer System
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 25: Instr. Sect. ID
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 26: Report Types
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 27: Reserved
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 28: Location of Specimen Collection
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 29: Nosocomial Infection Flag
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 30: Specimen Service
    // skipped
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 31: Specimen Institution
    // skipped

    // end of record
    sb.append("\r");

    return sb.toString().getBytes();
  }

  /**
   * Generates an L record.
   * <br>
   * Example: <STX>7L|1|N<CR><ETX>0A<CR><LF>
   * @param astmConfiguration the ASTM configuration to use for this record
   * @param record
   * @return
   */
  public static byte[] generateLRecord(AstmConfiguration astmConfiguration, Record record) {
    StringBuilder sb = new StringBuilder();
    // field 1: Record Identifier
    sb.append(RecordType.L);
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 2: Sequence Number
    sb.append("1");
    sb.append(astmConfiguration.getFieldDelimiter());

    // field 3: Termination Code
    sb.append("N");

    // end of record
    sb.append("\r");

    return sb.toString().getBytes();
  }

}
