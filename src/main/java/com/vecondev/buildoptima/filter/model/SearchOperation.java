package com.vecondev.buildoptima.filter.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SearchOperation {
  @JsonProperty("eq")
  EQ,

  @JsonProperty("like")
  LIKE,

  @JsonProperty("in")
  IN,

  @JsonProperty("gt")
  GT,

  @JsonProperty("ge")
  GE,

  @JsonProperty("lt")
  LT,

  @JsonProperty("le")
  LE,

  @JsonProperty("ne")
  NE
}
