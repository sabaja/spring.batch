package js.spring.batch.job.listener;

import js.spring.batch.dto.ExecutionContainer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static js.spring.batch.job.listener.ShopUtils.DATETIME_ARG;


@RequiredArgsConstructor
public class CreateUserStepListener implements StepExecutionListener {


    private final ExecutionContainer executionContainer;


    @Override
    public void beforeStep(@NonNull StepExecution stepExecution) {
        StepExecutionListener.super.beforeStep(stepExecution);
        JobExecution jobExecution = stepExecution.getJobExecution();
        DateTimeFormatter formatter = ShopUtils.DATE_TIME_FORMATTER;
        jobExecution.getExecutionContext().put(DATETIME_ARG, LocalDateTime.now().format(formatter));
        this.executionContainer.setJobExecution(jobExecution);
    }

    @Override
    public ExitStatus afterStep(@NonNull StepExecution stepExecution) {
        return StepExecutionListener.super.afterStep(stepExecution);
    }

}
