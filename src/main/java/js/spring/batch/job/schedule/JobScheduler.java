package js.spring.batch.job.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@EnableScheduling
@Component
public class JobScheduler {

    private static int i = 1;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier(value = "createFakeShopOrderJob")
    private Job job;


    @Scheduled(cron = "0 21 18 * * *")
    public void run() throws Exception {

        runJob();


        extracted();
    }

    private void runJob() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        String jobName = job.getName();
        log.info("{} is starting..", jobName);

        JobParameters launchDate = new JobParametersBuilder()
                .addLocalDateTime("launchDate", LocalDateTime.now())
                .toJobParameters();

        JobExecution run = jobLauncher.run(job, launchDate);

        ExitStatus exitStatus = run.getExitStatus();
        BatchStatus status = run.getStatus();

        log.info("{} is ended, with exit-code:{} and status:{}", jobName, exitStatus.getExitCode(), status.name());
    }

    private void extracted() throws InterruptedException {
        var expression = CronExpression.parse("10 * * * * *");
        var result = expression.next(LocalDateTime.now());
        System.out.println(result);

        log.info("Started : {}, {}", i, LocalDateTime.now());
        Thread.sleep(4000);
        log.info("Finished : {}, {} ", i++, LocalDateTime.now());
    }

    static class Location {
        String name;

        Location(String name) {
            this.name = name;
        }

        void setName(String name) {
            this.name = name;
        }

        String getName() {
            return this.name;
        }
    }

    public static void main(String[] args) {
        List<Location> locations = new ArrayList<>();
        locations.add(new Location("Ashburn"));
        locations.add(new Location("Broadlands"));

        Consumer<Location> capitalizeLocation = location -> location.setName(location.getName().toUpperCase());

        locations.forEach(capitalizeLocation); // Capitalize the Locations

        locations.forEach(location -> System.out.println(location.getName())); // Print capitalized locations
    }
}
