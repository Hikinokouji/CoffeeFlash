package sta.cfbe.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sta.cfbe.domain.exeption.ResourceNotFoundException;
import sta.cfbe.domain.user.User;
import sta.cfbe.repository.UserRepository;
import sta.cfbe.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public User create(User user) {
        if(userRepository.findByUsername(user.getPhoneNumber()).isPresent()){
            throw new IllegalStateException("Phone number already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.create(user);
        return user;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.delete(id);
    }
}
