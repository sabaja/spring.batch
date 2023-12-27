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
@Table(name = "shop_product")
public class ShopProductEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -7658157634134976766L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Integer id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "price")
    private Double price;
}