package sta.cfbe.web.dto.company;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String productName;
    private Integer price;
    private String imageUrl;
}
