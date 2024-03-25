package com.vecondev.buildoptima.filter.model;

import lombok.Getter;

@Getter
public enum DictionaryField {
  UPDATED_BY("updatedBy"),
  CATEGORY("category");

  private final String value;

  DictionaryField(String value) {
    this.value = value;
  }
}
