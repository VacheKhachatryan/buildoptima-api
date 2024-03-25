package com.vecondev.buildoptima.service.csv;

import com.vecondev.buildoptima.csv.CsvRecord;
import java.io.ByteArrayInputStream;
import java.util.List;

public interface CsvService<T extends CsvRecord> {

  ByteArrayInputStream writeToCsv(List<T> list, Class<T> clazz);
}
