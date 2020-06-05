package com.Repository;

import com.Model.Place;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public interface PlaceRepository extends CrudRepository<Place, Integer> {

}
