package js.spring.batch.repository;

import js.spring.batch.model.ShopUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ShopUserRepository extends JpaRepository<ShopUserEntity, Long> {

    ShopUserEntity findByUsername(String user);

    @Query(value = "FROM ShopUserEntity where createdAt >=:createdAt")
    List<ShopUserEntity> findAllByCreatedAtEqualsAfter(@Param("createdAt") LocalDateTime createdAt);
}
