package com.vecondev.buildoptima.filter.specification;

import static com.vecondev.buildoptima.exception.Error.INVALID_FIELD;
import static com.vecondev.buildoptima.exception.Error.INVALID_FILTER_STRUCTURE;
import static com.vecondev.buildoptima.filter.model.FilterConstants.NAME;
import static com.vecondev.buildoptima.filter.model.FilterConstants.OPERATION;
import static com.vecondev.buildoptima.filter.model.FilterConstants.VALUE;
import static com.vecondev.buildoptima.filter.model.FilterConstants.VALUES;

import com.vecondev.buildoptima.exception.InvalidFieldException;
import com.vecondev.buildoptima.filter.converter.SingleCriteriaConverter;
import com.vecondev.buildoptima.filter.model.Criteria;
import com.vecondev.buildoptima.filter.model.FieldDefinition;
import com.vecondev.buildoptima.filter.model.SearchOperation;
import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
public class GenericSpecification<T> implements Specification<T> {

  @Serial private static final long serialVersionUID = 863818768737625453L;

  private final transient Map<String, FieldDefinition<T, ?>> fieldDefinitionMap;
  private transient Map<String, Object> filter;
  private static final String OR = "or";
  private static final String AND = "and";

  @Nullable
  @Override
  public Predicate toPredicate(
      @NonNull Root<T> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder builder) {
    return convertTree(root, builder, filter);
  }

  private Predicate convertTree(Root<T> root, CriteriaBuilder builder, Map<String, Object> filter) {
    if (filter == null) {
      return builder.and();
    }

    if (!filter.isEmpty() && (filter.get(OR) == null && filter.get(AND) == null)) {
      if (!filter.containsKey(NAME)
          || !filter.containsKey(OPERATION)
              && (!filter.containsKey(VALUE) || !filter.containsKey(VALUES))) {
        throw new InvalidFieldException(INVALID_FILTER_STRUCTURE);
      }

      Criteria criteria = new Criteria();
      criteria.setName((String) filter.get(NAME));
      String operation = (String) filter.get(OPERATION);
      SearchOperation searchOperation = SearchOperation.valueOf(operation.toUpperCase());
      criteria.setOperation(searchOperation);
      if (filter.containsKey(VALUE)) {
        criteria.setValue((String) filter.get(VALUE));
      }
      if (filter.containsKey(VALUES)) {
        criteria.setValues((List<String>) filter.get(VALUES));
      }

      if (!fieldDefinitionMap.containsKey(criteria.getName())) {
        throw new InvalidFieldException(INVALID_FIELD);
      }

      return SingleCriteriaConverter.toPredicate(
          root, builder, searchOperation, criteria, fieldDefinitionMap.get(criteria.getName()));
    } else if (filter.get(OR) != null) {
      List<HashMap<String, Object>> values = (ArrayList) filter.get("or");
      return builder.or(
          values.stream()
              .map(stringObjectHashMap -> convertTree(root, builder, stringObjectHashMap))
              .toArray(Predicate[]::new));
    } else if (filter.containsKey(AND)) {
      List<HashMap<String, Object>> values = (ArrayList) filter.get("and");
      return builder.and(
          values.stream()
              .map(stringObjectHashMap -> convertTree(root, builder, stringObjectHashMap))
              .toArray(Predicate[]::new));
    } else {
      throw new InvalidFieldException(INVALID_FILTER_STRUCTURE);
    }
  }
}
