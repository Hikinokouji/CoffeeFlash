package sta.cfbe.web.controller.usercontrollers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sta.cfbe.domain.company.Company;
import sta.cfbe.domain.user.User;
import sta.cfbe.repository.CompanyRepository;
import sta.cfbe.repository.UserRepository;
import sta.cfbe.service.UserService;
import sta.cfbe.web.dto.user.UserResponse;
import sta.cfbe.web.mappers.UserMapper;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        List<Company> companies = companyRepository.findCompanyById(user.getId());
        user.setCompanies(new HashSet<>(companies));
        return userMapper.toUserResponse(user);
    }
}
