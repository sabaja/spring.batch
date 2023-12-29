package js.spring.batch.repository;

import js.spring.batch.model.ShopUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopUserRepository extends JpaRepository<ShopUserEntity, Long> {

    ShopUserEntity findByUsername(String user);
}
