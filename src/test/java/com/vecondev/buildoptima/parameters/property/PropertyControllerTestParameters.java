package com.vecondev.buildoptima.parameters.property;

import static com.vecondev.buildoptima.model.user.Role.CLIENT;
import static com.vecondev.buildoptima.model.user.Role.MODERATOR;

import com.vecondev.buildoptima.model.user.User;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class PropertyControllerTestParameters {

  private static final String TEST_FILES_PATH = "src/test/resources/files/";

  public List<User> users() {
    return List.of(
        new User("John", "Smith", "+712345678", "john@mail.ru", "John1234.", MODERATOR, true, 1),
        new User("John", "Stone", "+612345678", "john@gmail.com", "John1234/", CLIENT, true, 0));
  }

  public File getFile(String fileName) {
    Path path = Paths.get(TEST_FILES_PATH + fileName);
    if (Files.exists(path)) {
      return new File(String.valueOf(path));
    }
    return null;
  }
}
