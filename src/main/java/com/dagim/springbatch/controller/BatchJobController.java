package com.dagim.springbatch.controller;

import com.dagim.springbatch.entity.MessageResponse;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/batch-job")
public class BatchJobController {
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    @Qualifier(value = "csvReadJob")
    private Job job;

    @GetMapping(path = "/import", produces = "application/json")
    public ResponseEntity<?> importCsvJob(){

        JobParameters jobParameter = new JobParametersBuilder()
                .addLong("started At ... ", System.currentTimeMillis()).toJobParameters();
        try {
            jobLauncher.run(job, jobParameter);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(new MessageResponse(".... CSV Data processed successfully ..."),
                HttpStatus.OK);
    }

}
