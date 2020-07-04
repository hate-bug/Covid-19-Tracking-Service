package com.Repository;

import com.Model.Event;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.Date;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "event", path = "event")
public interface EventRepository extends PagingAndSortingRepository<Event, Long> {

    List<Event> findAllByNameAndDate (String name, Date date);

}
