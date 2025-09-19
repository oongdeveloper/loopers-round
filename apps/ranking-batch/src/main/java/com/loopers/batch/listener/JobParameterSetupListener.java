package com.loopers.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;

@Slf4j
@Component
public class JobParameterSetupListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        String targetDate = jobExecution.getJobParameters().getString("targetDate");

        String targetWeek = getWeek(targetDate);
        String targetMonth = getMonth(targetDate);

        jobExecution.getExecutionContext().put("targetWeek", targetWeek);
        jobExecution.getExecutionContext().put("targetMonth", targetMonth);

        log.info("===========================");
        log.info("배치 Parameter Setting.");
        log.info("targetDate = {}", targetDate);
        log.info("targetWeek = {}", targetWeek);
        log.info("targetMonth = {}", targetMonth);
        log.info("===========================");
    }

    private String getWeek(String targetDate){
        LocalDate date = LocalDate.parse(targetDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekNumber = date.get(weekFields.weekOfWeekBasedYear());
        int year = date.get(weekFields.weekBasedYear());
        return String.format("%d-%02d", year, weekNumber);
    }

    private String getMonth(String targetDate){
        LocalDate date = LocalDate.parse(targetDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
        YearMonth yearMonth = YearMonth.from(date);
        return yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }
}
