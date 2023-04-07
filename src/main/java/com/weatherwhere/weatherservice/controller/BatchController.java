package com.weatherwhere.weatherservice.controller;

import com.weatherwhere.weatherservice.config.BatchConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchController {
    private final BatchConfiguration batchConfiguration;
    private final JobLauncher jobLauncher;
    private final Job job;

    @GetMapping("/start")
    public String startBatch() throws Exception {
        batchConfiguration.initialize();
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution jobExecution = jobLauncher.run(job, jobParameters);

        return "!!!Batch Job Started: " + jobExecution.getStatus();
    }
}
