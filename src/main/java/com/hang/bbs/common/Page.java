package com.hang.bbs.common;

import lombok.Data;

import java.util.List;

/**
 * @author test
 */
@Data
public class Page<T> {

  private Integer number;
  private Integer size;
  private Integer totalPages;
  private Integer totalCount;
  private List<T> content;
  private Boolean first;
  private Boolean last;

  public Page(Integer number, Integer size, Integer totalCount, List<T> content) {
    this.number = number;
    this.size = size;
    this.totalCount = totalCount;
    this.content = content;
    this.totalPages = totalCount % size == 0 ? totalCount / size : (totalCount / size) + 1;
    this.first = number == 1;
    this.last = number.equals(this.totalPages);
  }

}
