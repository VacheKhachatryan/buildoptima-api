package com.vecondev.buildoptima.filter.model;

import static com.vecondev.buildoptima.filter.model.SearchOperation.EQ;
import static com.vecondev.buildoptima.filter.model.SearchOperation.GE;
import static com.vecondev.buildoptima.filter.model.SearchOperation.GT;
import static com.vecondev.buildoptima.filter.model.SearchOperation.IN;
import static com.vecondev.buildoptima.filter.model.SearchOperation.LE;
import static com.vecondev.buildoptima.filter.model.SearchOperation.LIKE;
import static com.vecondev.buildoptima.filter.model.SearchOperation.LT;
import static com.vecondev.buildoptima.filter.model.SearchOperation.NE;

import java.util.Set;
import lombok.Getter;

@Getter
public enum FieldType {
  STRING(Set.of(EQ, LIKE, NE, IN)),
  DOUBLE(Set.of(GT, GE, EQ, LE, LT, IN, NE)),
  LONG(Set.of(GT, GE, EQ, LE, LT, IN, NE)),
  BOOLEAN(Set.of(EQ, NE)),
  DATE(Set.of(GT, GE, EQ, LE, LT, IN, NE)),
  DATETIME(Set.of(GT, GE, EQ, LE, LT, IN, NE));

  private final Set<SearchOperation> operations;

  FieldType(Set<SearchOperation> operations) {
    this.operations = operations;
  }
}
