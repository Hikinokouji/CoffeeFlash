package sta.cfbe.service;

import sta.cfbe.web.dto.auth.JwtRequest;
import sta.cfbe.web.dto.auth.JwtResponse;

public interface AuthService {

    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);
}
