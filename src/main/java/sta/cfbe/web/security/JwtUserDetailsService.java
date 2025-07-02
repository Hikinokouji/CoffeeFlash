package sta.cfbe.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sta.cfbe.domain.user.User;
import sta.cfbe.service.UserService;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userPhone) throws UsernameNotFoundException {
        User user = userService.getByUsername(userPhone);
        return JwtEntityFactory.create(user);
    }
}
