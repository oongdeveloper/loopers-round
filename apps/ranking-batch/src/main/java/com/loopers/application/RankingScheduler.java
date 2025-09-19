package com.loopers.application;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class RankingScheduler {
    private final JobLauncher jobLauncher;
    private final Job rankingJob;

    @Scheduled(cron = "0 0 1 * * ?")
    public void rankingJob(){
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("targetDate",
                                LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(rankingJob, jobParameters);
            log.info("{} Job 실행 : {}", jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());
        } catch (JobExecutionAlreadyRunningException | JobRestartException |
                 JobInstanceAlreadyCompleteException | JobParametersInvalidException e){
            log.error("배치 Job Exception." , e);
        }
    }
}
