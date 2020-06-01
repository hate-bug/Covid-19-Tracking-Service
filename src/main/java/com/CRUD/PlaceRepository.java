package com.CRUD;

import com.Model.Place;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface PlaceRepository extends CrudRepository<Place, Integer> {

}
