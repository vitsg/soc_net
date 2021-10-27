package com.homework.social_network.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class CustomPageable extends PageRequest {
    private int offset;

    public CustomPageable(int offset, int limit, Sort sort) {
        super(offset, limit, sort);
        this.offset = offset;
    }

    public CustomPageable(int offset, int limit) {
        this(offset, limit, Sort.unsorted());
    }

    @Override
    public long getOffset() {
        return this.offset;
    }
}
