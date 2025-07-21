package sta.cfbe.web.dto.pos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SellingRequest {
    private List<ProductSellingDTO> products;
    private BigDecimal totalPrice;
    private int methodPayment;
}
