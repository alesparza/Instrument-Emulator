package com.github.alesparza.astm.protcol;

public class AstmConfiguration {

  /**
   * Default field delimiter: '|'
   */
  public static char DEFAULT_FIELD_DELIM = '|';

  /**
   * Default field delimiter: '\'
   */
  public static char DEFAULT_REPEAT_DELIM = '\\';

  /**
   * Default field delimiter: '!'
   */
  public static char DEFAULT_COMPONENT_DELIM = '!';

  /**
   * Default field delimiter: '~'
   */
  public static char DEFAULT_ESCAPE_DELIM = '~';

  /**
   * Default size of ASTM message: 64000
   */
  public static int DEFAULT_DATA_LENGTH = 64000;

  /**
   * Field delimiter used by this configuration.
   */
  char fieldDelimiter;

  /**
   * Repeat delimiter used by this configuration.
   */
  char repeatDelimiter;

  /**
   * Component delimiter used by this configuration.
   */
  char componentDelimiter;

  /**
   * Escape delimiter used by this configuration.
   */
  char escapeDelimiter;

  /**
   * Length of size of ASTM messages.
   */
  int dataLength;

  /**
   * Constructs a new ASTM configuration using the default delimiters.
   */
  public AstmConfiguration() {
    fieldDelimiter = DEFAULT_FIELD_DELIM;
    repeatDelimiter = DEFAULT_REPEAT_DELIM;
    componentDelimiter = DEFAULT_COMPONENT_DELIM;
    escapeDelimiter = DEFAULT_ESCAPE_DELIM;
    dataLength = DEFAULT_DATA_LENGTH;
  }

  /**
   * Constructs a new ASTM configuration using the default delimiters.
   * @param fieldDelimiter field delimiter to use
   * @param repeatDelimiter repeat delimiter to use
   * @param componentDelimiter component delimiter to use
   * @param escapeDelimiter escape delimiter to use
   */
  public AstmConfiguration(char fieldDelimiter, char repeatDelimiter, char componentDelimiter, char escapeDelimiter, int dataLength) {
    this.fieldDelimiter = fieldDelimiter;
    this.repeatDelimiter = repeatDelimiter;
    this.componentDelimiter = componentDelimiter;
    this.escapeDelimiter = escapeDelimiter;
    this.dataLength = dataLength;
  }

  public char getFieldDelimiter() {
    return fieldDelimiter;
  }

  public char getRepeatDelimiter() {
    return repeatDelimiter;
  }

  public char getComponentDelimiter() {
    return componentDelimiter;
  }

  public char getEscapeDelimiter() {
    return escapeDelimiter;
  }

  public int getDataLength() {
    return dataLength;
  }

  public void setFieldDelimiter(char fieldDelimiter) {
    this.fieldDelimiter = fieldDelimiter;
  }

  public void setRepeatDelimiter(char repeatDelimiter) {
    this.repeatDelimiter = repeatDelimiter;
  }

  public void setComponentDelimiter(char componentDelimiter) {
    this.componentDelimiter = componentDelimiter;
  }

  public void setEscapeDelimiter(char escapeDelimiter) {
    this.escapeDelimiter = escapeDelimiter;
  }

  public void setDataLength(int dataLength) {
    this.dataLength = dataLength;
  }
}
