package com.vecondev.buildoptima.filter.model;

import static com.vecondev.buildoptima.filter.model.FieldType.DATETIME;
import static com.vecondev.buildoptima.filter.model.FieldType.STRING;

import com.vecondev.buildoptima.filter.converter.InstantConverter;
import com.vecondev.buildoptima.filter.converter.RoleConverter;
import com.vecondev.buildoptima.model.user.User;
import java.util.Map;

public class UserFields {

  public static final Map<String, FieldDefinition<User, ?>> userPageSortingFieldsMap;
  private static final String FIRST_NAME = "firstName";
  private static final String LAST_NAME = "lastName";
  private static final String EMAIL = "email";
  private static final String PHONE = "phone";
  private static final String ROLE = "role";
  private static final String CREATED_AT = "createdAt";
  private static final String UPDATED_AT = "updatedAt";

  static {
    userPageSortingFieldsMap =
        Map.of(
            FIRST_NAME,
            new FieldDefinition<User, String>(FIRST_NAME, STRING, FIRST_NAME),
            LAST_NAME,
            new FieldDefinition<User, String>(LAST_NAME, STRING, LAST_NAME),
            EMAIL,
            new FieldDefinition<User, String>(EMAIL, STRING, EMAIL),
            PHONE,
            new FieldDefinition<User, String>(PHONE, STRING, PHONE),
            ROLE,
            new FieldDefinition<>(ROLE, STRING, ROLE, new RoleConverter()),
            CREATED_AT,
            new FieldDefinition<>(CREATED_AT, DATETIME, CREATED_AT, new InstantConverter()),
            UPDATED_AT,
            new FieldDefinition<>(UPDATED_AT, DATETIME, UPDATED_AT, new InstantConverter()));
  }

  private UserFields() {}
}
