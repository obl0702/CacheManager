package com.ongbl.caffeinecache.config;

import com.ongbl.caffeinecache.model.AAAPojo;
import com.ongbl.caffeinecache.service.MyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class SimpleJobConfig {

    @Bean
    @Qualifier("simpleJob")
    public Job simpleJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .tasklet(firstTask(),transactionManager).build();
    }

    @Bean
    public Tasklet firstTask(){
        return new Tasklet(){

            @Autowired
            MyService myService;

            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                log.info("This is first tasklet step");
                log.info("SEC = " + chunkContext.getStepContext().getStepExecutionContext());
                List<String> list = new ArrayList<>();
                list.add("AAA");
                list.add("BBB");

                JobExecution jobExecution = chunkContext.getStepContext().getStepExecution().getJobExecution();
                String myParam = jobExecution.getJobParameters().getString("param");
                log.info(myParam);

                myService.updateCachedData("AAAA","1111");
                myService.updateCachedData("BBBB","2222");

                String aaaa = (String) myService.getCachedData("AAAA", String.class);
                String bbbb = (String) myService.getCachedData("BBBB", String.class);

                log.info("AAAA: " + aaaa);
                log.info("BBBB: " + bbbb);

                myService.evictCache("AAAA");
                aaaa = (String) myService.getCachedData("AAAA", String.class);
                bbbb = (String) myService.getCachedData("BBBB", String.class);
                log.info("AAAA: " + aaaa);
                log.info("BBBB: " + bbbb);

                //myService.evictWholeCache();
                aaaa = (String) myService.getCachedData("AAAA", String.class);
                bbbb = (String) myService.getCachedData("BBBB", String.class);
                log.info("AAAA: " + aaaa);
                log.info("BBBB: " + bbbb);

                AAAPojo a1 = new AAAPojo();
                a1.setName("Name1");
                a1.setDesc("Desc1");
                myService.updateCachedData("A1",a1);

                AAAPojo a2 = new AAAPojo();
                a2.setName("Name2");
                a2.setDesc("Desc2");
                myService.updateCachedData("A2",a2);

                AAAPojo a3 = (AAAPojo) myService.getCachedData("A1", AAAPojo.class);
                AAAPojo a4 = (AAAPojo) myService.getCachedData("A2", AAAPojo.class);

                log.info("A1: " + a3.toString());
                log.info("A2: " + a4.toString());

                log.info("ZZ ZZZ1: "+ myService.getCachedData2("ZZZ1"));
                log.info("ZZ ZZZ2: "+ myService.getCachedData2("ZZZ2"));
                log.info("ZZ ZZZ1 2nd: "+ myService.getCachedData2("ZZZ1"));
                log.info("ZZ ZZZ2 2nd: "+ myService.getCachedData2("ZZZ2"));
                log.info("ZZ XXXX: "+ myService.getCachedData2("XXXX"));
                log.info("ZZ XXXX: "+ myService.getCachedData2("XXXX"));


                return RepeatStatus.FINISHED;
            }
        };
    }
}
