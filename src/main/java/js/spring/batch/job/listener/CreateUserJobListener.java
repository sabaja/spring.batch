package js.spring.batch.job.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import static js.spring.batch.job.listener.ShopUtils.DATETIME_ARG;

public class CreateUserJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        JobExecutionListener.super.beforeJob(jobExecution);

        jobExecution.getExecutionContext().put(DATETIME_ARG, "2023-12-01 00:00:00.000");

        System.out.println(jobExecution.getExecutionContext().getString(DATETIME_ARG));
    }
}
