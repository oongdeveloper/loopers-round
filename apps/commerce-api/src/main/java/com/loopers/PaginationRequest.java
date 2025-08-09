package com.loopers;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
public class PaginationRequest {
    private Integer page;
    private Integer size;

    public Pageable toPageable(){
        if (page == null && size == null){
            return PageConstants.defaultPageable();
        }

        int reqPage = (page == null || page <= 0) ? 1 : page;
        int reqSize = (size == null || size <= 0) ? PageConstants.DEFAULT_PAGE_SIZE : size;
        reqSize = Math.min(reqSize, PageConstants.DEFAULT_PAGE_SIZE);

        return PageRequest.of(reqPage, reqSize);
    }

    public static class PageConstants{
        static int DEFAULT_PAGE_NUMS = 0;
        static int DEFAULT_PAGE_SIZE = 20;

        public static Pageable defaultPageable(){
            return PageRequest.of(DEFAULT_PAGE_NUMS, DEFAULT_PAGE_SIZE);
        }
    }
}
