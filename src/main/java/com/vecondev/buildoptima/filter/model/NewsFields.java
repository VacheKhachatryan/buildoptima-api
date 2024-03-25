package com.vecondev.buildoptima.filter.model;

import static com.vecondev.buildoptima.filter.model.FieldType.DATETIME;
import static com.vecondev.buildoptima.filter.model.FieldType.STRING;

import com.vecondev.buildoptima.filter.converter.InstantConverter;
import com.vecondev.buildoptima.filter.converter.NewsCategoryConverter;
import com.vecondev.buildoptima.filter.converter.StatusConverter;
import com.vecondev.buildoptima.filter.converter.UUIDConverter;
import com.vecondev.buildoptima.model.news.News;
import java.util.Map;

public class NewsFields {

  public static final Map<String, FieldDefinition<News, ?>> newsPageSortingFieldsMap;
  private static final String TITLE = "title";
  private static final String SUMMARY = "summary";
  private static final String DESCRIPTION = "description";
  private static final String STATUS = "status";
  private static final String CATEGORY = "category";
  private static final String CREATED_AT = "createdAt";
  private static final String UPDATED_AT = "updatedAt";
  private static final String CREATED_BY = "createdBy";
  private static final String UPDATED_BY = "updatedBy";

  static {
    newsPageSortingFieldsMap =
        Map.of(
            TITLE,
            new FieldDefinition<News, String>(TITLE, STRING, TITLE),
            SUMMARY,
            new FieldDefinition<News, String>(SUMMARY, STRING, SUMMARY),
            STATUS,
            new FieldDefinition<>(STATUS, STRING, STATUS, new StatusConverter()),
            DESCRIPTION,
            new FieldDefinition<News, String>(DESCRIPTION, STRING, DESCRIPTION),
            CATEGORY,
            new FieldDefinition<>(CATEGORY, STRING, CATEGORY, new NewsCategoryConverter()),
            CREATED_AT,
            new FieldDefinition<>(CREATED_AT, DATETIME, CREATED_AT, new InstantConverter()),
            UPDATED_AT,
            new FieldDefinition<>(UPDATED_AT, DATETIME, UPDATED_AT, new InstantConverter()),
            CREATED_BY,
            new FieldDefinition<>(CREATED_BY, STRING, CREATED_BY, new UUIDConverter()),
            UPDATED_BY,
            new FieldDefinition<>(UPDATED_BY, STRING, UPDATED_BY, new UUIDConverter()));
  }

  private NewsFields() {}
}
