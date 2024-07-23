package com.github.alesparza.emulator.assay;

import java.util.ArrayList;

public class Assay {

  /**
   * Name of the assay.
   */
  String name;

  /**
   * Interface code for the assay.
   */
  String code;

  /**
   * Result of the assay.
   */
  String result;

  /**
   * Units of the assay.
   */
  String units;

  /**
   * Date assay completed.
   */
  String completeDate;

  /**
   * List of comments attached to the assay.
   */
  ArrayList<String> comments;

  /**
   * Constructs a new assay with name and test code only (the bare minimum to exist).
   * @param name the name of the assay
   * @param code the interface code of the assay
   */
  public Assay(String name, String code) {
    this.name = name;
    this.code = code;
    this.comments = new ArrayList<>();
  }

  /**
   * Constructs a new assay with name and test code and a result (the bare minimum to transmit).
   * @param name the name of the assay
   * @param code the interface code of the assay
   * @param result the result of the assay
   */
  public Assay(String name, String code, String result) {
    this.name = name;
    this.code = code;
    this.result = result;
    this.comments = new ArrayList<>();
  }

  public String getName() {
    return name;
  }

  public String getCode() {
    return code;
  }

  public String getResult() {
    return result;
  }

  public String getUnits() {
    return units;
  }

  public String getCompleteDateTime() {
    return completeDate;
  }

  public ArrayList<String> getComments() {
    return comments;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public void setUnits(String units) {
    this.units = units;
  }

  public void setCompleteDate(String completeDate) {
    this.completeDate = completeDate;
  }

  /**
   * Gets a printable single-line comment of all stored comments.
   * @return a single-line comment
   */
  public String getPrintableComments() {
    String ret = "";
    for (String comment : comments) {
      ret = ret + comment + "~|~";
    }
    if (ret.endsWith("~|~")) {
      ret = ret.substring(0, ret.length() - 3);
    }
    return ret;
  }

  /**
   * Add a comment to this assay.
   * @param comment the comment to add
   */
  public void addComment(String comment) {
    comments.add(comment);
  }

  /**
   * Resets the comments for this assay.
   */
  public void resetComments() {
    comments.clear();
  }
}
