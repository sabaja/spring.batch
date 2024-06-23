package js.spring.batch.repository;

import js.spring.batch.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String user);

    @Query(value = "FROM UserEntity where createdAt >=:createdAt")
    List<UserEntity> findAllByCreatedAtEqualsAfter(@Param("createdAt") LocalDateTime createdAt);
}
