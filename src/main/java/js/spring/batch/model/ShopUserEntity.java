package js.spring.batch.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;

@Getter
@Setter
@Entity
@Table(name = "shop_user")
public class ShopUserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -3072266355966911096L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    private String username;

    private String email;
}