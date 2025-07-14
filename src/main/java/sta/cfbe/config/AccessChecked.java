package sta.cfbe.config;

import liquibase.exception.LiquibaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sta.cfbe.domain.company.Company;
import sta.cfbe.domain.exeption.AccessDeniedException;
import sta.cfbe.repository.CompanyRepository;
import sta.cfbe.service.CompanyService;
import sta.cfbe.service.LiquibaseService;
import sta.cfbe.service.UserService;
import sta.cfbe.web.security.JwtTokenProvider;

import java.sql.SQLException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccessChecked {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CompanyRepository companyRepository;
    private final LiquibaseService liquibaseService;

    public boolean existsByUserIdAndCompanyId(String authHeader, String companyId) throws LiquibaseException {

        try{
            if(companyRepository.findCompanyByUuid(companyId).isPresent()){
                liquibaseService.runLiquibaseForTenant(companyId);
                Long userId = jwtTokenProvider.tokenIdForController(authHeader);
                return userService.existsByUserIdAndCompanyId(userId, companyId);
            }
            throw new AccessDeniedException("Access denied: 013");
        }catch (SQLException e) {
            throw new AccessDeniedException("Access denied: 014");
        }
    }
}

//Задача цього компонента перевіряти доступність користувача до інших компаній.
//        Виключити випадки коли користувач може під своїм токеном робити запити на інші компанії
//    Окрім загального фільтру тепер до кожного запиту в контройлер, додається токен та перевіряється
//        чи є зв'язок цього користувача в базі по ManyToMany зв'язку. JPA генерує запит перевіряє та надсилає
//        відповідь true/false а далі користувач або залишається не авторизованим або переходить до
//        ! Рекомендовано в майбутньому розглянути інший варіант перевірки так як ми створюємо таким чином додаткове
//        навантаження на Базу
