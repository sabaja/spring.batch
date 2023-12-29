package js.spring.batch.job.writer;

import js.spring.batch.dto.ShopOrderDto;
import js.spring.batch.model.ShopOrderEntity;
import js.spring.batch.model.ShopProductEntity;
import js.spring.batch.model.ShopUserEntity;
import js.spring.batch.repository.ShopOrderRepository;
import js.spring.batch.repository.ShopProductRepository;
import js.spring.batch.repository.ShopUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreateShopWriter implements ItemWriter<ShopOrderDto> {



    private final ShopProductRepository shopProductRepository;
    private final ShopUserRepository shopUserRepository;
    private final ShopOrderRepository shopOrderRepository;

    private ShopOrderEntity orderEntity;
    private ShopOrderDto orderDto;

    @Override
    public void write(Chunk<? extends ShopOrderDto> chunk) throws Exception {

        log.info("Starting Shop Writing....");
        chunk.getItems().forEach(order -> {
            /* TODO - da verificare se già presente*/
            orderDto = order;
            ShopOrderEntity entity = new ShopOrderEntity();
            entity.setQuantity(order.getQuantity());
            entity.setOrderDate(LocalDateTime.now());
            orderEntity = shopOrderRepository.saveAndFlush(entity);

            /* TODO - da gestire le eccezioni*/
            saveShopUser();
            saveProductUser();
        });

        log.info("End Shop Writing");
    }

    private void saveShopUser() {
        if (Objects.nonNull(orderEntity) && Objects.nonNull(orderDto)) {

            ShopUserEntity shopUserEntity = shopUserRepository.findByUsername(orderDto.getUser());
            shopUserEntity.getOrders().add(orderEntity);
            shopUserRepository.save(shopUserEntity);
        }
    }
    private void saveProductUser() {
        if (Objects.nonNull(orderEntity) && Objects.nonNull(orderDto)) {
            ShopProductEntity shopProductEntity = shopProductRepository.findByProductName(orderDto.getProduct());
            shopProductEntity.getOrders().add(orderEntity);
            shopProductRepository.save(shopProductEntity);
        }
    }
}
