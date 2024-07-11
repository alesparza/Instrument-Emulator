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
   * Constructs a new ASTM configuration using the default delimiters.
   */
  public AstmConfiguration() {
    fieldDelimiter = DEFAULT_FIELD_DELIM;
    repeatDelimiter = DEFAULT_REPEAT_DELIM;
    componentDelimiter = DEFAULT_COMPONENT_DELIM;
    escapeDelimiter = DEFAULT_ESCAPE_DELIM;
  }

  /**
   * Constructs a new ASTM configuration using the default delimiters.
   * @param fieldDelimiter field delimiter to use
   * @param repeatDelimiter repeat delimiter to use
   * @param componentDelimiter component delimiter to use
   * @param escapeDelimiter escape delimiter to use
   */
  public AstmConfiguration(char fieldDelimiter, char repeatDelimiter, char componentDelimiter, char escapeDelimiter) {
    this.fieldDelimiter = fieldDelimiter;
    this.repeatDelimiter = repeatDelimiter;
    this.componentDelimiter = componentDelimiter;
    this.escapeDelimiter = escapeDelimiter;
  }

}
