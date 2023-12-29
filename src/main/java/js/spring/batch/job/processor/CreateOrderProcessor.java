package js.spring.batch.job.processor;

import js.spring.batch.dto.ShopOrderDto;
import js.spring.batch.model.ShopOrderEntity;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class CreateOrderProcessor implements ItemProcessor<ShopOrderDto, ShopOrderEntity> {

    @Override
    public ShopOrderEntity process(ShopOrderDto item) throws Exception {


        return null;
    }
}
