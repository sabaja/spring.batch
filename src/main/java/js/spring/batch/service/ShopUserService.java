package js.spring.batch.service;

import js.spring.batch.model.UserEntity;
import js.spring.batch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ShopUserService {

    private final UserRepository userRepository;


    public Optional<List<UserEntity>> findAllShopUsersByCreatedAtEqualsAndAfter(LocalDateTime createdAt) {
        return Optional.ofNullable(userRepository.findAllByCreatedAtEqualsAfter(createdAt));
    }
}
