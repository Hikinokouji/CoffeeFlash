package sta.cfbe.service;

import sta.cfbe.domain.user.User;

public interface UserService {

    User getById(Long id);

    User getByUsername(String userPhone);

    User create(User user);

    void delete(Long id);

}
