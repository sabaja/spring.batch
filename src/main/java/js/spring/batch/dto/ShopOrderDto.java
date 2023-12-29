package js.spring.batch.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


@Data
@NoArgsConstructor
public class ShopOrderDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 3960729831517120224L;

    private String product;
    private String user;
    private Integer quantity;
}
