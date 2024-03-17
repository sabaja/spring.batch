package js.spring.batch;

import js.spring.batch.model.BatchChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BatchChunkRepository extends JpaRepository<BatchChunk, Integer> {
    Optional<BatchChunk> findByJobName(String jobName);
}
