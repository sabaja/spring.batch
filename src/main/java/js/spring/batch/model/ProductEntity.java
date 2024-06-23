package js.spring.batch.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "shop_product")
public class ProductEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -7658157634134976766L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Integer id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<OrderEntity> orders;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "price")
    private Double price;
}