package js.spring.batch.job.processor;

import js.spring.batch.dto.ShopOrderDto;
import js.spring.batch.model.ShopOrderEntity;
import js.spring.batch.model.ShopProductEntity;
import js.spring.batch.model.ShopUserEntity;
import js.spring.batch.repository.ShopProductRepository;
import js.spring.batch.repository.ShopUserRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CreateOrderProcessor implements ItemProcessor<ShopOrderDto, ShopOrderEntity> {

    @Override
    public ShopOrderEntity process(ShopOrderDto item) throws Exception {


        return null;
    }
}
