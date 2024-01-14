package js.spring.batch.service;

import jakarta.persistence.EntityManager;
import js.spring.batch.model.ShopUserEntity;
import js.spring.batch.repository.ShopUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ShopUserService {

    private final ShopUserRepository shopUserRepository;

    private final EntityManager entityManager;

    @Transactional(rollbackFor = Exception.class)
    public void deleteAllInBatch(List<ShopUserEntity> shopUserEntities) {
        entityManager.flush();
        shopUserRepository.deleteAllInBatch(shopUserEntities);
    }

    public Optional<List<ShopUserEntity>> findAllShopUsersByCreatedAtEqualsAndAfter(LocalDateTime createdAt) {
        return Optional.ofNullable(shopUserRepository.findAllByCreatedAtEqualsAfter(createdAt));
    }
}
