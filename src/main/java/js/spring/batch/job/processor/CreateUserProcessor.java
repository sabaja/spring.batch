package js.spring.batch.job.processor;

import js.spring.batch.dto.ExecutionContainer;
import js.spring.batch.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import static js.spring.batch.job.listener.ShopUtils.DATETIME_ARG;


@Slf4j
@AllArgsConstructor
@Service
public class CreateUserProcessor implements ItemProcessor<UserEntity, UserEntity> {


    private ExecutionContainer executionContainer;

    @Override
    public UserEntity process(UserEntity item) {
        JobExecution jobExecution = this.executionContainer.getJobExecution();
        String dateTime = String.valueOf(jobExecution.getExecutionContext().get(DATETIME_ARG));

        log.info("Params from JobContext-> {}", dateTime);
        log.info(item.toString());
        return item;
    }
}

