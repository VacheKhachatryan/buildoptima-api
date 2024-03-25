package com.vecondev.buildoptima.parameters.faq.category;

import static com.vecondev.buildoptima.filter.model.SearchOperation.EQ;
import static com.vecondev.buildoptima.filter.model.SearchOperation.GT;

import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.filter.model.Criteria;
import com.vecondev.buildoptima.filter.model.SortDto;
import java.util.List;
import java.util.Map;

public class FaqCategoryTestParameters {

  public FetchRequestDto getFetchRequest() {
    return new FetchRequestDto(
        Map.of(
            "and",
            List.of(
                new Criteria(EQ, "name", "Royalties"),
                Map.of(
                    "or",
                    List.of(
                        new Criteria(GT, "createdAt", "2018-11-30T18:35:24.00Z"),
                        new Criteria(GT, "updatedAt", "2018-11-30T18:35:24.00Z"))))),
        0,
        10,
        List.of(new SortDto("name", SortDto.Direction.ASC)));
  }
}
