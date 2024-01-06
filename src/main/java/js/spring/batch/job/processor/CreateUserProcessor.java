package js.spring.batch.job.processor;

import js.spring.batch.model.ShopUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class CreateUserProcessor implements ItemProcessor<ShopUserEntity, ShopUserEntity> {
    @Override
    public ShopUserEntity process(ShopUserEntity item) throws Exception {

        log.info(item.toString());
        return item;
    }
}
