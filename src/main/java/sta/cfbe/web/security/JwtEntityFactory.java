package sta.cfbe.web.security;

import sta.cfbe.domain.user.User;

public class JwtEntityFactory {
    public static JwtEntity create(User user) {
        return new JwtEntity(
                user.getId(),
                user.getPhoneNumber(),
                user.getPassword());
    }
}
