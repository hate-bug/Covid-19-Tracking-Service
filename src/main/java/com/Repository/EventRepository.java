package com.Repository;

import com.Model.Event;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.Date;
import java.util.List;

public interface EventRepository extends PagingAndSortingRepository<Event, Long> {

    List<Event> findAllByNameAndDate (String name, Date date);

    Iterable<Event> findAllByNameContainingIgnoreCase (String name);

}
