package js.spring.batch.job;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Slf4j
@Component
@EnableScheduling
public class ScheduledJob {

    @Setter
    private boolean isJobEnabled = true;

    @Qualifier(value = "myJob")
    @Autowired
    private Job job;

    @Autowired
    private JobLauncher jobLauncher;

    @Scheduled(fixedDelay = 5000)
    public void execute() throws Exception {
        if (isJobEnabled) {
            JobParameters jobParameters = new JobParametersBuilder().addString("time", LocalDateTime.now().toString()).toJobParameters();

            JobExecution execution = jobLauncher.run(job, jobParameters);

            log.info("Job Exit Status :: {}", execution.getExitStatus().getExitCode());
        }
    }

}
