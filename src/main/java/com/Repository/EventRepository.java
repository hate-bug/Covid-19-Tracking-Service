package com.Repository;

import com.Model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

public interface EventRepository extends PagingAndSortingRepository<Event, Integer> {

    public List<Event> findAllByNameAndDate(String name, Date date);

}
