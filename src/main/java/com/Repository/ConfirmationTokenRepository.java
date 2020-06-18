package com.Repository;

import com.Model.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;

public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, Long> {

    public ConfirmationToken findByConfirmationToken (String token);
}
