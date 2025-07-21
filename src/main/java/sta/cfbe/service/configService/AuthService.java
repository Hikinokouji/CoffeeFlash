package sta.cfbe.service.configService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import sta.cfbe.entity.user.User;
import sta.cfbe.service.admin.UserService;
import sta.cfbe.web.dto.auth.JwtRequest;
import sta.cfbe.web.dto.auth.JwtResponse;
import sta.cfbe.web.security.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtResponse login(JwtRequest loginRequest) {
        JwtResponse jwtResponse = new JwtResponse();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getPhoneNumber(), loginRequest.getPassword()));
        User user = userService.getByUsername(loginRequest.getPhoneNumber());
        jwtResponse.setId(user.getId());
        jwtResponse.setPhoneNumber(user.getPhoneNumber());
        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(user.getId(), user.getPhoneNumber()));
        jwtResponse.setRefreshToken(jwtTokenProvider.createRefreshToken(user.getId(), user.getPhoneNumber()));
        jwtResponse.setCompanyUuid(userService.findCompanyByUser(user));
        return jwtResponse;
    }

    public JwtResponse refresh(String refreshToken) {
        return jwtTokenProvider.refreshUserToken(refreshToken);
    }

}
