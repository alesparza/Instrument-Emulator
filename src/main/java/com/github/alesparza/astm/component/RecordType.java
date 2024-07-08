package com.github.alesparza.astm.component;

/**
 * Specific type of records.
 */
public enum RecordType {
  H, P, O, R, C, S, M, Q, L;

  /**
   * Gets the character of the record.
   * @return the character of the record
   */
  public char getRecordIdentifierChar() {
    return name().charAt(0);
  }
}
