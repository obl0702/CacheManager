package com.ongbl.caffeinecache.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SimpleScheduler {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("simpleJob")
    private Job simpleJob;

    @Scheduled(fixedDelay = 5000)
    //@Scheduled(cron = "15 * * * * *")
    public void scheduleJob() throws JobExecutionException {

        String batchId = String.valueOf(System.currentTimeMillis());

        JobParameters jobParams1 = new JobParametersBuilder()
                .addString("param", "param1")
                .addString("run.id", batchId)
                .toJobParameters();
        JobParameters jobParams2 = new JobParametersBuilder()
                .addString("param", "param2")
                .addString("run.id", batchId)
                .toJobParameters();
        JobParameters jobParams3 = new JobParametersBuilder()
                .addString("param", "param3")
                .addString("run.id", batchId)
                .toJobParameters();

        jobLauncher.run(simpleJob, jobParams1);
    }
}
