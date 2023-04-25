package com.weatherwhere.weatherservice.controller.weathermid;

import com.weatherwhere.weatherservice.config.weathermid.MidBatchConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class MidBatchController {
    private final MidBatchConfiguration midBatchConfiguration;
    private final JobLauncher jobLauncher;
    private Job job;

    /**
     * 특정 job만 실행시키기 위해 Qualifier 어노테이션 사용후 setJob 설정
     *
     * @param job
     */
    @Autowired
    @Qualifier("jpaJobMid")
    public void setJob(Job job) {
        this.job = job;
    }

    /**
     * 중기예보 batch 돌리는 api
     *
     * @return Batch 작업이 성공적으로 수행을 시작했음을 알리는 String 리턴
     * @throws Exception
     */
    @GetMapping("/batch/mid")
    public String startBatch() throws Exception {
        midBatchConfiguration.initialize();
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution jobExecution = jobLauncher.run(this.job, jobParameters);

        return "!!!Batch Job Started: " + jobExecution.getStatus();
    }
}
