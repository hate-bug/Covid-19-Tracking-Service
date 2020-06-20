package com.WebController;

import com.Model.Applicant;
import com.Repository.ApplicantRepository;
import com.Service.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Optional;

@RestController
public class ApplicationController {

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private MessageSender messageSender;

    @PostMapping (value = "/postapplication")
    public Applicant postApplication (@RequestBody Applicant applicant) throws FileAlreadyExistsException {
        //check if any applicantion already existed
        if (this.applicantRepository.findAllByApplicantEmailAndDescription(applicant.getApplicantEmail(), applicant.getDescription()).size()==0){
            return this.applicantRepository.save(applicant);
        }
        throw new FileAlreadyExistsException("File exists");
    }

    @GetMapping (value = "/admin/getapplicants")
    public Iterable<Applicant> getAllApplicants (@RequestParam("page") int page){
        return this.applicantRepository.findAll(PageRequest.of(page-1, 10));
    }

    @PostMapping (value = "/admin/approveapplicant")
    public Applicant approveApplicant (@RequestBody long applicantId) throws FileNotFoundException {
        Optional<Applicant> applicant = this.applicantRepository.findById(applicantId);
        if (applicant.isPresent()){
            //send email to the applicant.
            this.messageSender.sendRegisterEmail(applicant.get());
            return applicant.get();
        } else {
            throw new FileNotFoundException("No such applicant.");
        }
    }

    @DeleteMapping (value = "/admin/deleteapplicant")
    public void DeclineApplicant (@RequestParam("id") long id){
        this.applicantRepository.deleteById(id);
    }


}
