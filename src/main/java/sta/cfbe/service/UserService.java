package sta.cfbe.service;

import sta.cfbe.domain.company.Company;
import sta.cfbe.domain.user.User;
import sta.cfbe.web.dto.user.UserResponse;

import java.util.Optional;
import java.util.Set;

public interface UserService {

    User getById(Long id);

    User getByUsername(String phoneNumber);

    User create(User user);

    boolean existsByUserIdAndCompanyId(Long userId, String companyId);

    Set<String> findCompanyByUser(User user);
}
