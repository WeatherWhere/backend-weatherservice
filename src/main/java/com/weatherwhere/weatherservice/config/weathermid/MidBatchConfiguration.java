package com.weatherwhere.weatherservice.config.weathermid;

import com.weatherwhere.weatherservice.config.JobCompletionNotificationListener;
import com.weatherwhere.weatherservice.domain.weathermid.WeatherMidEntity;
import com.weatherwhere.weatherservice.dto.weathermid.RegionCodeDTO;
import com.weatherwhere.weatherservice.service.date.DateService;
import com.weatherwhere.weatherservice.service.weathermid.ParseCSVService;
import com.weatherwhere.weatherservice.service.weathermid.WeatherMidService;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class MidBatchConfiguration {
    private final WeatherMidService weatherMidService;
    private final ParseCSVService parseCSVService;
    private final DateService dateService;

    private List<RegionCodeDTO> regionCodeDTOList;
    private String[] threeToSevenDays;
    private List<WeatherMidEntity> collectData;
    private String tmfc;

    // 리스트의 데이터를 하나씩 인덱스를 통해 가져온다.
    private int nextIndex;

    public void initialize() {
        regionCodeDTOList = parseCSVService.ParseCSV();
        threeToSevenDays = dateService.getDaysAfterToday(3, 7);
        tmfc = dateService.getTmfc();
        collectData = weatherMidService.makeEntityList(regionCodeDTOList, threeToSevenDays, tmfc);
        nextIndex = 0;
    }

    // ItemReader
    @Bean
    public ItemReader<WeatherMidEntity> restItCollectReaderMid() {
        return new ItemReader<WeatherMidEntity>() {
            @Override
            public WeatherMidEntity read() {
                // ItemReader는 반복문으로 동작한다.
                // 하나씩 ItemWriter로 전달해야 한다.
                WeatherMidEntity nextCollect = null;

                if (nextIndex < collectData.size()) {
                    // 전체 리스트에서 하나씩 추출해서, 하나씩 Writer로 전달한다.
                    nextCollect = collectData.get(nextIndex);
                    System.out.println(nextIndex);
                    nextIndex++;
                }
                return nextCollect;
            }
        };
    }
    // ItemWriter
    @Bean
    public JpaItemWriter<WeatherMidEntity> jpaItemWriterMid(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<WeatherMidEntity> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

    // step
    @Bean
    public Step jpaStepMid(JobRepository jobRepository,
                        PlatformTransactionManager transactionManager, JpaItemWriter<WeatherMidEntity> writer) {
        return new StepBuilder("jpaStepMid", jobRepository)
                .<WeatherMidEntity, WeatherMidEntity>chunk(30, transactionManager)
                .reader(restItCollectReaderMid())
                .writer(writer)
                .build();
    }

    // job
    @Bean
    public Job jpaJobMid(JobRepository jobRepository, JobCompletionNotificationListener listener, Step jpaStepMid) {
        return new JobBuilder("jpaJobMid", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(jpaStepMid)
                .end()
                .build();
    }
}
