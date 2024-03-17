package js.spring.batch.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "batch_chunk")
public class BatchChunk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "job_name", unique = true)
    private String jobName;

    @Min(value = 1)
    @Column(name = "chunk_size")
    private Long chunkSize;

}
