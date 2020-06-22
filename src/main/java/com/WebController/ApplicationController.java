package com.WebController;

import com.Model.Applicant;
import com.Repository.ApplicantRepository;
import com.Service.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;
import java.io.FileNotFoundException;
import java.util.Optional;

@RestController
public class ApplicationController {

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private MessageSender messageSender;

    @PostMapping (value = "/postapplication")
    public Applicant postApplication (@RequestBody Applicant applicant) throws InstanceAlreadyExistsException {
        if (this.applicantRepository.existsByApplicantEmail(applicant.getApplicantEmail())){
            throw new InstanceAlreadyExistsException("Email already exists.");
        }
        return this.applicantRepository.save(applicant);
    }

    @GetMapping (value = "/admin/getapplicants")
    public Iterable<Applicant> getAllApplicants (@RequestParam("page") int page){
        return this.applicantRepository.findAll(PageRequest.of(page-1, 10));
    }

    @PostMapping (value = "/admin/approveapplicant")
    public void approveApplicant (@RequestBody long applicantId) throws FileNotFoundException {
        Optional<Applicant> applicant = this.applicantRepository.findById(applicantId);
        if (applicant.isPresent()){
            //send email to the applicant.
            this.messageSender.sendRegisterEmail(applicant.get());
            this.applicantRepository.deleteById(applicant.get().getId());
        } else {
            throw new FileNotFoundException("No such applicant.");
        }
    }

    @DeleteMapping (value = "/admin/deleteapplicant")
    public void DeclineApplicant (@RequestParam("id") long id){
        this.applicantRepository.deleteById(id);
    }


}
