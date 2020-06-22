package com.Repository;

import com.Model.Applicant;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ApplicantRepository extends PagingAndSortingRepository<Applicant, Long> {
    boolean existsByApplicantEmail (String email);
}
