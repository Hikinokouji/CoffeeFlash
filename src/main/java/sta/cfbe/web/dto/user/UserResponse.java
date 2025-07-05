package sta.cfbe.web.dto.user;

import lombok.Data;
import sta.cfbe.domain.company.Company;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class UserResponse {
    private Long id;
    private String phoneNumber;
    private Set<Company> companySet = new HashSet<>();
}
