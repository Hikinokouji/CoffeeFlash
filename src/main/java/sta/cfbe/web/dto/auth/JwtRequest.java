package sta.cfbe.web.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JwtRequest {

    @NotNull(message = "phoneNumber must be not null.")
    private String phoneNumber;
    @NotNull(message = "Password must be not null.")
    private String password;

}
