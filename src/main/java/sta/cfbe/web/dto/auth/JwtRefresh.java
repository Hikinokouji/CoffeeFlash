package sta.cfbe.web.dto.auth;

import lombok.Data;

@Data
public class JwtRefresh {
    private String refreshToken;
}
