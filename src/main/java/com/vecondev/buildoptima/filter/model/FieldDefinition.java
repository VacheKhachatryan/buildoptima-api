package com.vecondev.buildoptima.filter.model;

import java.util.function.Function;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import org.springframework.core.convert.converter.Converter;

public class FieldDefinition<T, Y extends Comparable<Y>> {

  private final String searchParamName;

  private final String entityFieldName;

  private final FieldType type;

  private final Function<Root<T>, Path<Y>> function;

  private final Converter<String, Y> converter;

  public FieldDefinition(
      String searchParamName,
      FieldType type,
      String entityFieldName,
      Converter<String, Y> converter,
      Function<Root<T>, Path<Y>> function) {
    this.searchParamName = searchParamName;
    this.type = type;
    this.entityFieldName = entityFieldName;
    this.converter = converter;
    this.function = function;
  }

  @SuppressWarnings("unchecked")
  public FieldDefinition(String searchParamName, FieldType type, String entityFieldName) {
    this(searchParamName, type, entityFieldName, convert -> (Y) convert, path -> (Path<Y>) path);
  }

  @SuppressWarnings("unchecked")
  public FieldDefinition(
      String searchParamName,
      FieldType type,
      String entityFieldName,
      Converter<String, Y> converter) {
    this(searchParamName, type, entityFieldName, converter, path -> (Path<Y>) path);
  }

  public String getSearchParamName() {
    return searchParamName;
  }

  public FieldType getType() {
    return type;
  }

  public Path<Y> getPath(Root<T> root) {
    return function.apply(root);
  }

  public String getEntityFieldName() {
    return entityFieldName;
  }

  public Y convertValue(String s) {
    return converter.convert(s);
  }
}
