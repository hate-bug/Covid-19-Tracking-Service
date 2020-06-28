package com.Repository;

import com.Model.Event;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.Date;

public interface EventRepository extends PagingAndSortingRepository<Event, Integer> {

    public Event findByNameAndDate (String name, Date date);

}
