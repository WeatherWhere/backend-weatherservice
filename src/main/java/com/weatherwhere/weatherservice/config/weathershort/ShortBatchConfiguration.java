package com.weatherwhere.weatherservice.config.weathershort;

import com.weatherwhere.weatherservice.config.JobCompletionNotificationListener;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortMain;
import com.weatherwhere.weatherservice.domain.weathershort.WeatherShortSub;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortEntityListDTO;
import com.weatherwhere.weatherservice.dto.weathershort.WeatherShortRequestDTO;
import com.weatherwhere.weatherservice.service.weathershort.WeatherShortMainService;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class ShortBatchConfiguration{
    private final WeatherShortMainService weatherShortMainService;
    private WeatherShortEntityListDTO collectData;

    // 리스트의 데이터를 하나씩 인덱스를 통해 가져온다.
    List<WeatherShortMain> mainEntityList = new ArrayList<>();
    List<WeatherShortSub> subEntityList = new ArrayList<>();

    public void initialize() throws Exception {

        WeatherShortRequestDTO weatherShortRequestDTO = new WeatherShortRequestDTO();
        String baseDate = "20230409";
        String baseTime = "0500";
        weatherShortRequestDTO.setBaseDate(baseDate);
        weatherShortRequestDTO.setBaseTime(baseTime);
        collectData = weatherShortMainService.getXYListWeatherAllSave(weatherShortRequestDTO,
                mainEntityList, subEntityList) ;

    }

    //itemReader(main)
    private ItemReader<WeatherShortMain> restItCollectReaderShortMain() {
        //여러개의 step을 병렬처리할때는 데이터 소실등의 문제가 발생할 수 있는데
        //이때 AtomicInteger를 사용해서 index를 구하면 원자적으로 증가시키기때문에
        //데이터 유실 가능성이 적어진다.
        AtomicInteger nextIndex = new AtomicInteger(0);
        return new ItemReader<WeatherShortMain>() {
            @Override
            public WeatherShortMain read() {
                // ItemReader는 반복문으로 동작한다.
                // 하나씩 ItemWriter로 전달해야 한다.
                WeatherShortMain mainEntity = null;
                int index = nextIndex.getAndIncrement();
                if (index < collectData.getMainEntityList().size()) {
                    // 전체 리스트에서 하나씩 추출해서, 하나씩 Writer로 전달한다.
                    mainEntity = collectData.getMainEntityList().get(index);
                    System.out.println(index);
                    System.out.println("=================="+collectData.getMainEntityList().size());
                }
                return mainEntity;
            }
        };
    }

    // ItemWriter(main)
    @Bean
    public JpaItemWriter<WeatherShortMain> jpaItemWriterMain(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<WeatherShortMain> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

    //ItemReader(sub)
    private ItemReader<WeatherShortSub> restItCollectReaderShortSub() {
        AtomicInteger nextIndex = new AtomicInteger(0);
        return new ItemReader<WeatherShortSub>() {
            @Override
            public WeatherShortSub read() {
                WeatherShortSub subEntity = null;
                int index = nextIndex.getAndIncrement();
                if (index < collectData.getSubEntityList().size()) {
                    // 전체 리스트에서 하나씩 추출해서, 하나씩 Writer로 전달한다.
                    subEntity = collectData.getSubEntityList().get(index);
                    System.out.println(index);
                }
                return subEntity;
            }
        };
    }

    // ItemWriter(sub)
    @Bean
    public JpaItemWriter<WeatherShortSub> jpaItemWriterSub(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<WeatherShortSub> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

    // step1(main entity)
    @Bean
    public Step jpaStepShortMain(JobRepository jobRepository,
                                 PlatformTransactionManager transactionManager, JpaItemWriter<WeatherShortMain> writer) {
        return new StepBuilder("jpaStepShortMain", jobRepository)
                .<WeatherShortMain, WeatherShortMain>chunk(30, transactionManager)
                .reader(restItCollectReaderShortMain())
                .writer(writer)
                .build();
    }

    // step2(sub entity)
    @Bean
    public Step jpaStepShortSub(JobRepository jobRepository,
                                PlatformTransactionManager transactionManager, JpaItemWriter<WeatherShortSub> writer) {
        return new StepBuilder("jpaStepShortSub", jobRepository)
                .<WeatherShortSub, WeatherShortSub>chunk(30, transactionManager)
                .reader(restItCollectReaderShortSub())
                .writer(writer)
                .build();
    }

    // job
    @Bean
    public Job jpaJobShort(JobRepository jobRepository, JobCompletionNotificationListener listener, Step jpaStepShortMain, Step jpaStepShortSub) {
        //병렬 처리할 flow 생성(main, sub)
        Flow flow1 = new FlowBuilder<Flow>("flow1")
                .start(jpaStepShortMain) // Step1 을 가진 플로우를 생성해줍니다.
                .build();

        Flow flow2 = new FlowBuilder<Flow>("flow2")
                .start(jpaStepShortSub) // Step2 을 가진 플로우를 생성해줍니다.
                .build();

        //SimpleAsyncTaskExecutor -> step 병렬처리(main entity, sub entity 동시에 read, writer 가능하게)
        return new JobBuilder("jpaJobShort", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(flow1)
                .split(new SimpleAsyncTaskExecutor())
                .add(flow2)
                .end()
                .build();
    }
}