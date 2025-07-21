package sta.cfbe.web.dto.pos;

import lombok.Data;

@Data
public class ProductSellingDTO {
    private Long id;
    private Integer priceSell;
    private Integer quantity;
}
