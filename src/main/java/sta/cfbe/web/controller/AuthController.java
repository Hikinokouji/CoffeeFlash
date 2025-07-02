package sta.cfbe.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sta.cfbe.domain.user.User;
import sta.cfbe.service.AuthService;
import sta.cfbe.service.UserService;
import sta.cfbe.web.dto.auth.JwtRequest;
import sta.cfbe.web.dto.auth.JwtResponse;
import sta.cfbe.web.dto.user.UserDTO;
import sta.cfbe.web.mappers.UserMapper;

@RestController
@RequestMapping("/auth/")
@RequiredArgsConstructor
@Validated
@Tag(name="AuthController", description = "Auth API")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;


    @PostMapping("/login")
    @Operation(summary = "Some description")
    public JwtResponse login(@RequestBody JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public UserDTO register(@Validated @RequestBody UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User createdUser = userService.create(user);
        return userMapper.toUserDTO(createdUser);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody String refreshToken) {
        return authService.refresh(refreshToken);
    }
}
