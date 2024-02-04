package js.spring.batch.dto;

import lombok.Data;
import org.springframework.batch.core.JobExecution;

@Data
public class ExecutionContainer {

    private JobExecution jobExecution;
}
