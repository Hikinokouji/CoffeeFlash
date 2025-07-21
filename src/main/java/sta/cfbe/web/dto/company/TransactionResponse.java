package sta.cfbe.web.dto.company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class TransactionResponse {
    private String id;
    private Timestamp created_at;
    private String methodPayment;
    private BigDecimal totalprice;
}
