package sta.cfbe.web.dto.auth;

import lombok.Data;

@Data
public class JwtResponse {

    private Long id;
    private String userPhone;
    private String accessToken;
    private String refreshToken;

}
