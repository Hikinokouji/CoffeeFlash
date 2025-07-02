package sta.cfbe.domain.company;

import lombok.Data;

import java.util.UUID;

@Data
public class Company {
    private Long id;
    private UUID company_uuid;
    private String company_name;
    //private User user_id;
}
