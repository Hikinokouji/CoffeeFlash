package sta.cfbe.web.dto.user;

import lombok.Data;
import sta.cfbe.entity.company.Company;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserResponse {
    private Long id;
    private String phoneNumber;
    private Set<Company> companySet = new HashSet<>();
}
