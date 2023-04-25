package com.weatherwhere.weatherservice.controller.weathershort;


import com.weatherwhere.weatherservice.config.weathershort.ShortBatchConfiguration;
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
public class ShortBatchController {

    private final ShortBatchConfiguration shortBatchConfiguration;
    private final JobLauncher jobLauncher;

    private Job job;

    /**
     * 특정 job만 실행시키기 위해 Qualifier어노테이션 사용 후 setJob해주기
     *
     * @param job
     */
    @Autowired
    @Qualifier("jpaJobShort")
    public void setJob(Job job) {
        this.job = job;
    }


    /**
     * 단기예보 batch 돌리는 api
     *
     * @return 성공했는지 여부와 총 걸린시간 String으로 리턴
     * @throws Exception
     */
    @GetMapping("/batch/short")
    public String shortStartBatch() throws Exception {
        Long startTime = System.currentTimeMillis();
        shortBatchConfiguration.initialize();
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution jobExecution = jobLauncher.run(this.job, jobParameters);
        Long endTime = System.currentTimeMillis();

        return "!!!Batch Job Started: " + jobExecution.getStatus() +"총 걸린시간:"+ (endTime-startTime);
    }


}
