package js.spring.batch.job.listener;

import js.spring.batch.model.ShopUserEntity;
import js.spring.batch.service.ShopUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CreateUserStepListener implements StepExecutionListener {

    private final ShopUserService shopUserService;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        StepExecutionListener.super.beforeStep(stepExecution);
        String createdAtStr = "2023-12-01 00:00:00.000";
        DateTimeFormatter formatter = ShopUtils.DATE_TIME_FORMATTER;
        Optional<List<ShopUserEntity>> users = shopUserService.findAllShopUsersByCreatedAtEqualsAndAfter(LocalDateTime.parse(createdAtStr, formatter));
        users.ifPresent(shopUserService::deleteAllInBatch);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return StepExecutionListener.super.afterStep(stepExecution);
    }
}
