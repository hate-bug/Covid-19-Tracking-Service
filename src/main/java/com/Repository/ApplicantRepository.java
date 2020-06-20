package com.Repository;

import com.Model.Applicant;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.List;

public interface ApplicantRepository extends PagingAndSortingRepository<Applicant, Long> {
    List<Applicant> findAllByApplicantEmailAndDescription (String ApplicantEmail, String Description);
}
