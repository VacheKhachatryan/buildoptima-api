package com.vecondev.buildoptima.service.csv;

import static com.vecondev.buildoptima.exception.Error.FAILED_CSV_CONVERTING;

import com.vecondev.buildoptima.csv.CsvRecord;
import com.vecondev.buildoptima.csv.Header;
import com.vecondev.buildoptima.exception.ConvertingFailedException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CsvServiceImpl<T extends CsvRecord> implements CsvService<T> {

  public ByteArrayInputStream writeToCsv(List<T> list, Class<T> clazz) {
    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      CSVPrinter csvPrinter =
          new CSVPrinter(
              new PrintWriter(outputStream), CSVFormat.DEFAULT.withHeader(getHeaders(clazz)));
      for (T entity : list) {
        csvPrinter.printRecord(entity.getAllFieldValues());
      }
      csvPrinter.flush();
      return new ByteArrayInputStream(outputStream.toByteArray());
    } catch (IOException ex) {
      throw new ConvertingFailedException(FAILED_CSV_CONVERTING);
    }
  }

  private String[] getHeaders(Class<T> clazz) {
    return Arrays.stream(clazz.getDeclaredFields())
        .map(field -> field.getAnnotation(Header.class).value())
        .toArray(String[]::new);
  }
}
