package js.spring.batch.repository;

import js.spring.batch.model.ShopProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopProductRepository extends JpaRepository<ShopProductEntity, Integer> {

    ShopProductEntity findByProductName(String productName);
}