package js.spring.batch.job.processor;

import js.spring.batch.dto.ExecutionContainer;
import js.spring.batch.model.ShopUserEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import static js.spring.batch.job.listener.ShopUtils.DATETIME_ARG;


@Slf4j
@AllArgsConstructor
@Service
public class CreateUserProcessor implements ItemProcessor<ShopUserEntity, ShopUserEntity> {


    private ExecutionContainer executionContainer;

    @Override
    public ShopUserEntity process(ShopUserEntity item) throws Exception {
        JobExecution jobExecution = this.executionContainer.getJobExecution();
        String dateTime = String.valueOf(jobExecution.getExecutionContext().get(DATETIME_ARG));
//        LocalDateTime dateTime = stepExecution.getJobExecution().getJobParameters().getLocalDateTime(DATETIME_ARG);

        log.info("Params from JobContext-> {}", dateTime);
        log.info(item.toString());
        return item;
    }


//    @BeforeStep
//    public void beforeStep(StepExecution stepExecution) {
//        JobParameters jobParameters = stepExecution.getJobParameters();
//
//
//        this.dateTime = jobParameters.getString(DATETIME_ARG);
//    }


//    @Override
//    public ExitStatus afterStep(StepExecution stepExecution) {
//        return StepExecutionListener.super.afterStep(stepExecution);
//    }
}

