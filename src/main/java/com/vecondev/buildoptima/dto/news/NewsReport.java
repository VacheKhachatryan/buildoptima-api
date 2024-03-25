package com.vecondev.buildoptima.dto.news;

import com.vecondev.buildoptima.dto.news.response.NewsResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewsReport {

  private List<NewsResponseDto> newsList;
}
