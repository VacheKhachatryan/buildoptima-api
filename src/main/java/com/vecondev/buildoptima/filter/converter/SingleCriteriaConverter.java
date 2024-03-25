package com.vecondev.buildoptima.filter.converter;

import static lombok.AccessLevel.PRIVATE;

import com.vecondev.buildoptima.filter.model.Criteria;
import com.vecondev.buildoptima.filter.model.FieldDefinition;
import com.vecondev.buildoptima.filter.model.SearchOperation;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class SingleCriteriaConverter {

  public static <T, Y extends Comparable<Y>> Predicate toPredicate(Root<T> root,
      CriteriaBuilder builder,
      SearchOperation operation,
      Criteria criteria,
      FieldDefinition<T, Y> fieldDefinition) {

    Path<Y> path = fieldDefinition.getPath(root);
    return switch (operation) {
      case NE -> builder.notEqual(path.get(fieldDefinition.getEntityFieldName()),
          fieldDefinition.convertValue(criteria.getValue()));
      case EQ -> builder.equal(
          path.get(fieldDefinition.getEntityFieldName()),
          fieldDefinition.convertValue(criteria.getValue()));
      case LIKE -> builder.like(
          builder.lower(path.get(fieldDefinition.getEntityFieldName())),
          ("%" + fieldDefinition.convertValue(criteria.getValue()) + "%").toLowerCase());
      case GT -> builder.greaterThan(
          path.get(fieldDefinition.getEntityFieldName()),
          fieldDefinition.convertValue(criteria.getValue()));
      case LT -> builder.lessThan(
          path.get(fieldDefinition.getEntityFieldName()),
          fieldDefinition.convertValue(criteria.getValue()));
      case GE -> builder.greaterThanOrEqualTo(
          path.get(fieldDefinition.getEntityFieldName()),
          fieldDefinition.convertValue(criteria.getValue()));
      case LE -> builder.lessThanOrEqualTo(
          path.get(fieldDefinition.getEntityFieldName()),
          fieldDefinition.convertValue(criteria.getValue()));
      case IN -> builder.in(
          path.get(fieldDefinition.getEntityFieldName())).value(criteria.getValues().stream()
          .map(fieldDefinition::convertValue).toList());
    };
  }

}
