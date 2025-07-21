package sta.cfbe.web.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import sta.cfbe.entity.company.Company;
import sta.cfbe.entity.user.User;
import sta.cfbe.repository.admins.UserRepository;
import sta.cfbe.repository.admins.CompanyRepository;
import sta.cfbe.service.admin.UserService;
import sta.cfbe.web.dto.user.UserResponse;
import sta.cfbe.web.mappers.UserMapper;
import sta.cfbe.web.security.JwtTokenProvider;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/admins")
    public UserResponse getUserById(@RequestHeader("Authorization") String authHeader) {
        User user = userService.getById(jwtTokenProvider.tokenIdForController(authHeader));
        List<Company> companies = companyRepository.findCompanyById(user.getId());
        user.setCompanies(new HashSet<>(companies));
        return userMapper.toUserResponse(user);
    }
}
