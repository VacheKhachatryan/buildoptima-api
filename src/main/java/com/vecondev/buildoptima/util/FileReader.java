package com.vecondev.buildoptima.util;

import java.io.BufferedReader;
import java.io.IOException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FileReader {

  /**
   * Reads a json file in given path.
   *
   * @param path the given json file path
   * @return on Object of json file
   */
  public Object readFromJson(String path) {
    try (BufferedReader reader = new BufferedReader(new java.io.FileReader(path))) {
      StringBuilder builder = new StringBuilder();
      String read;
      while ((read = reader.readLine()) != null) {
        builder.append(read);
      }
      return builder.toString();
    } catch (IOException e) {
      throw new IllegalStateException("Could not read file");
    }
  }
}
