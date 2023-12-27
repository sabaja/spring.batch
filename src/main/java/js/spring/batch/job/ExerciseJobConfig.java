package js.spring.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class ExerciseJobConfig {

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager, Tasklet tasklet1) {
        log.info("Building step");
        return new StepBuilder("myTasklet", jobRepository)
                .tasklet(tasklet1, transactionManager)
                .allowStartIfComplete(true)
                .build();

    }

    @Bean
    public Job myJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info("Start job");
        return new JobBuilder("myJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(stepOne(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step stepOne(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("stepOne", jobRepository)
                .tasklet(myTasklet(), transactionManager)
                .build();
    }


    @Bean
    public Tasklet myTasklet() {
        log.info("Building tasklet");
        return (stepContribution, chunkContext) -> {
            log.info("Running MyTasklet");
            return RepeatStatus.FINISHED;
        };
    }
}