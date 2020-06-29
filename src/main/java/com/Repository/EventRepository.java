package com.Repository;

import com.Model.Event;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.Date;
import java.util.List;

public interface EventRepository extends PagingAndSortingRepository<Event, Integer> {

    public List<Event> findAllByNameAndDate (String name, Date date);

}
