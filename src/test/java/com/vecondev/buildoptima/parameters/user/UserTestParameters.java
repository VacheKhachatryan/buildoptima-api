package com.vecondev.buildoptima.parameters.user;

import static com.vecondev.buildoptima.filter.model.SearchOperation.EQ;
import static com.vecondev.buildoptima.filter.model.SearchOperation.GT;
import static com.vecondev.buildoptima.filter.model.SearchOperation.LIKE;

import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.filter.model.Criteria;
import com.vecondev.buildoptima.filter.model.SortDto;
import java.util.List;
import java.util.Map;

public class UserTestParameters {

  public FetchRequestDto getFetchRequest() {
    return new FetchRequestDto(
        Map.of(
            "and",
            List.of(
                new Criteria(EQ, "firstName", "John"),
                Map.of(
                    "or",
                    List.of(
                        new Criteria(LIKE, "lastName", "Smith"),
                        new Criteria(GT, "createdAt", "2018-11-30T18:35:24.00Z"))))),
        0,
        10,
        List.of(new SortDto("firstName", SortDto.Direction.ASC)));
  }
}
