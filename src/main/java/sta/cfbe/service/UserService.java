package sta.cfbe.service;

import sta.cfbe.domain.user.User;

public interface UserService {

    User getById(Long id);

    User getByUsername(String phoneNumber);

    User create(User user);
}
