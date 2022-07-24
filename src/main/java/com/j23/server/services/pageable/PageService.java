package com.j23.server.services.pageable;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PageService {

    public Pageable findByFieldOrder(int page, int size, String orderedFieldName, int order) {

        if (orderedFieldName == null) {
            return PageRequest.of(page, size);
        }
        return PageRequest.of(page, size, Sort.by(order == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, orderedFieldName));
    }
}
