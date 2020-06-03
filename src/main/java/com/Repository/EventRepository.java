package com.Repository;

import com.Model.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public interface EventRepository extends CrudRepository<Event, Integer> {


}
