package sta.cfbe.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionOperations;
import sta.cfbe.domain.company.Company;
import sta.cfbe.domain.exeption.AccessDeniedException;
import sta.cfbe.domain.exeption.resource.ResourceNotFoundException;
import sta.cfbe.domain.user.User;
import sta.cfbe.repository.CompanyRepository;
import sta.cfbe.repository.UserRepository;
import sta.cfbe.service.CompanyService;
import sta.cfbe.service.UserService;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final CompanyService companyService;
    private final PasswordEncoder passwordEncoder;
    private final TransactionOperations transactionOperations;

    @Override
    //@Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserIdAndCompanyId(Long userId, String companyId) {
        try{
            boolean result = userRepository.existsByIdAndCompanies_CompanyUuid(userId, companyId);
            if(result) {
                return true;
            }
            throw new ResourceNotFoundException("Access Denied : 011");
            //error list
        }catch (Exception ex) {
            throw new ResourceNotFoundException("Access Denied : 012");
            //error list
        }
    }

    @Override
    public Set<String> findCompanyByUser(User user) {

        Set<String> companies = companyRepository.findCompaniesByUserId(user.getId());
        return companies;
    }

    @Override
    public User create(User user) {
        if(userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()){
            throw new IllegalStateException("Phone number already in use");
        }
        Optional<Company> company = companyService.createCompany();
        return transactionOperations.execute(status -> {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            company.ifPresent(c -> user.getCompanies().add(c));
            return userRepository.save(user);

        });
    }
}
