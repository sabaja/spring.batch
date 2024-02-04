package js.spring.batch.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@ToString
@Table(name = "shop_user")
public class ShopUserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -3072266355966911096L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<ShopOrderEntity> orders;

    private String username;

    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}