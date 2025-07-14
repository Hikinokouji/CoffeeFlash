package sta.cfbe.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import sta.cfbe.domain.company.Company;
import sta.cfbe.domain.user.User;
import sta.cfbe.repository.CompanyRepository;
import sta.cfbe.repository.UserRepository;
import sta.cfbe.service.UserService;
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
