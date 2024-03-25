package com.vecondev.buildoptima.filter.model;

import static com.vecondev.buildoptima.filter.model.FieldType.DATETIME;
import static com.vecondev.buildoptima.filter.model.FieldType.STRING;
import static lombok.AccessLevel.PRIVATE;

import com.vecondev.buildoptima.filter.converter.InstantConverter;
import com.vecondev.buildoptima.filter.converter.StatusConverter;
import com.vecondev.buildoptima.model.faq.FaqQuestion;
import java.util.Map;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class FaqQuestionFields {

  public static final Map<String, FieldDefinition<FaqQuestion, ?>> faqQuestionPageSortingFieldsMap;

  private static final String QUESTION = "question";
  private static final String ANSWER = "answer";
  private static final String STATUS = "status";
  private static final String CREATED_AT = "createdAt";
  private static final String UPDATED_AT = "updatedAt";
  private static final String UPDATED_BY = "updatedBy";
  private static final String CREATED_BY = "createdBy";
  private static final String CATEGORY = "category";

  private static final String USER_ID = "id";
  private static final String CATEGORY_NAME = "name";

  static {
    faqQuestionPageSortingFieldsMap =
        Map.of(
            QUESTION,
            new FieldDefinition<FaqQuestion, String>(QUESTION, STRING, QUESTION),
            ANSWER,
            new FieldDefinition<FaqQuestion, String>(ANSWER, STRING, ANSWER),
            STATUS,
            new FieldDefinition<>(STATUS, STRING, STATUS, new StatusConverter()),
            CREATED_AT,
            new FieldDefinition<>(CREATED_AT, DATETIME, CREATED_AT, new InstantConverter()),
            UPDATED_AT,
            new FieldDefinition<>(UPDATED_AT, DATETIME, UPDATED_AT, new InstantConverter()),
            CREATED_BY,
            new FieldDefinition<>(
                CREATED_BY, STRING, USER_ID, s -> s, root -> root.join(CREATED_BY)),
            UPDATED_BY,
            new FieldDefinition<>(
                UPDATED_BY, STRING, USER_ID, s -> s, root -> root.join(UPDATED_BY)),
            CATEGORY,
            new FieldDefinition<>(
                CATEGORY, STRING, CATEGORY_NAME, s -> s, root -> root.join(CATEGORY)));
  }
}
