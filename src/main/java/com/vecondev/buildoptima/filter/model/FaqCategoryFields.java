package com.vecondev.buildoptima.filter.model;

import static com.vecondev.buildoptima.filter.model.FieldType.DATETIME;
import static com.vecondev.buildoptima.filter.model.FieldType.STRING;
import static lombok.AccessLevel.PRIVATE;

import com.vecondev.buildoptima.filter.converter.InstantConverter;
import com.vecondev.buildoptima.model.faq.FaqCategory;
import java.util.Map;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class FaqCategoryFields {

  public static final Map<String, FieldDefinition<FaqCategory, ?>> faqCategoryPageSortingFieldsMap;
  private static final String NAME = "name";
  private static final String CREATED_AT = "createdAt";
  private static final String UPDATED_AT = "updatedAt";
  private static final String UPDATED_BY = "updatedBy";
  private static final String CREATED_BY = "createdBy";

  private static final String USER_ID = "id";

  static {
    faqCategoryPageSortingFieldsMap =
        Map.of(
            NAME,
            new FieldDefinition<FaqCategory, String>(NAME, STRING, NAME),
            CREATED_AT,
            new FieldDefinition<>(CREATED_AT, DATETIME, CREATED_AT, new InstantConverter()),
            UPDATED_AT,
            new FieldDefinition<>(UPDATED_AT, DATETIME, UPDATED_AT, new InstantConverter()),
            CREATED_BY,
            new FieldDefinition<>(
                CREATED_BY, STRING, USER_ID, s -> s, root -> root.join(CREATED_BY)),
            UPDATED_BY,
            new FieldDefinition<>(
                UPDATED_BY, STRING, USER_ID, s -> s, root -> root.join(UPDATED_BY)));
  }
}
