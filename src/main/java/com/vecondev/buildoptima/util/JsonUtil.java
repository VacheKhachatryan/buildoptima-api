package com.vecondev.buildoptima.util;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY;
import static com.vecondev.buildoptima.exception.Error.FAILED_JSON_CONVERTING;

import com.amazonaws.services.s3.event.S3EventNotification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vecondev.buildoptima.dto.property.PropertyListDto;
import com.vecondev.buildoptima.exception.AuthenticationException;
import com.vecondev.buildoptima.exception.ConvertingFailedException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class JsonUtil {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public String writeToJson(Object object) {
    try {
      OBJECT_MAPPER.registerModule(new JavaTimeModule());
      OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      return OBJECT_MAPPER.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new ConvertingFailedException(FAILED_JSON_CONVERTING);
    }
  }

  public PropertyListDto readFromJson(File file) throws IOException {
    OBJECT_MAPPER.configure(ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    PropertyListDto propertyListDto = OBJECT_MAPPER.readValue(file, new TypeReference<>() {});
    log.info("Trying to remove processed file from local storage");
    FileUtil.deleteFile(file);
    return propertyListDto;
  }

  public List<String> getListOfAuthoritiesFromJsonString(String jsonString) {
    try {
      return OBJECT_MAPPER.readValue(jsonString, new TypeReference<>() {
      });
    } catch (Exception ex) {
      throw new AuthenticationException(FAILED_JSON_CONVERTING);
    }
  }

  public S3EventNotification getNotification(String message) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    return mapper.readValue(message, new TypeReference<>() {});
  }
}
