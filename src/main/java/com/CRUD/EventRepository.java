package com.CRUD;

import com.Model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface EventRepository extends CrudRepository<Event, Integer> {

    List<Event> findAll ();

}
