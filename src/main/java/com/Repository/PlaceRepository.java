package com.Repository;

import com.Model.Place;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(collectionResourceRel = "place", path = "place")
public interface PlaceRepository extends CrudRepository<Place, Long> {

}
