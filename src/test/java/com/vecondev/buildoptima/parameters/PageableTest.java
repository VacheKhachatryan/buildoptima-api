package com.vecondev.buildoptima.parameters;

import static com.vecondev.buildoptima.exception.Error.INVALID_PAGEABLE;

import com.vecondev.buildoptima.dto.filter.FetchRequestDto;
import com.vecondev.buildoptima.exception.InvalidFieldException;
import com.vecondev.buildoptima.filter.model.SortDto;
import java.util.ArrayList;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface PageableTest {

  default Pageable getPageable(FetchRequestDto fetchRequest) {
    Sort sort = Sort.unsorted();
    int skip = fetchRequest.getSkip() != null ? fetchRequest.getSkip() : 0;
    int take = fetchRequest.getTake() != null ? fetchRequest.getTake() : 10;
    int page = 0;

    if (take > 0) {
      if (skip % take != 0) {
        throw new InvalidFieldException(INVALID_PAGEABLE);
      }

      page = skip / take;
    }
    if (fetchRequest.getSort() == null) {
      fetchRequest.setSort(new ArrayList<>());
    }
    for (SortDto sortDto : fetchRequest.getSort()) {
      sort =
          sort.and(
              Sort.by(Sort.Direction.fromString(sortDto.getOrder().name()), sortDto.getField()));
    }
    return new PageRequest(page, take, sort) {};
  }
}
