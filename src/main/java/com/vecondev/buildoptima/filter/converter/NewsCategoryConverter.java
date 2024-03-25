package com.vecondev.buildoptima.filter.converter;

import com.vecondev.buildoptima.model.news.NewsCategory;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

public class NewsCategoryConverter implements Converter<String, NewsCategory> {

  @Override
  public NewsCategory convert(@NonNull String category) {
    return NewsCategory.valueOf(category);
  }
}
