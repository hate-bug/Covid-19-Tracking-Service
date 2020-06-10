package com.Repository;

import com.Model.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Component
public interface EventRepository extends CrudRepository<Event, Integer> {

    public List<Event> findAllByNameAndDate(String name, Date date);

}
