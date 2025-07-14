package sta.cfbe.web.dto.auth;

import lombok.Data;
import sta.cfbe.domain.company.Company;

import java.util.Set;

@Data
public class JwtResponse {

    private Long id;
    private String phoneNumber;
    private String accessToken;
    private String refreshToken;
    private Set<String> companyUuid;

}
