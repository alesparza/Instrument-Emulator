package com.github.alesparza.astm.component;

import java.util.ArrayList;

/**
 * Represents a ASTM record.
 */
public abstract class Record {

  /**
   * List of fields.
   */
  Field[] fields;

  /**
   * Constructs a new Record with a specific number of fields.
   */
  public Record(int maxFields) {
    fields = new Field[maxFields + 1];
  }

  /**
   * Gets a specific field.
   * @param idx the ASTM Field number index.  Note that ASTM Field Numbers are 1-indexed.  Follow specification.
   * @return the field at the specified index, or <code>null</code> if the index is invalid
   */
  public Field getField(int idx) {
    if (idx < 1 || idx >= fields.length + 1) {
      return null;
    }
    return fields[idx - 1];
  }

  /**
   * Sets a specific field.
   * @param idx the ASTM Field number index.  Note that ASTM Field Numbers are 1-indexed.  Follow specification.
   * @param field the Field to store
   */
  public void setField(int idx, Field field) {
    if (idx < 1 || idx >= fields.length + 1) {
      return;
    }
    fields[idx - 1] = field;
  }
}
