package js.spring.batch.repository;

import js.spring.batch.model.ShopOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopOrderEntityRepository extends JpaRepository<ShopOrderEntity, Integer> {
}