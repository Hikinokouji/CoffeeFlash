package sta.cfbe.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sta.cfbe.service.UserService;
import sta.cfbe.web.security.JwtTokenProvider;

@Component
@RequiredArgsConstructor
public class AccessChecked {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public boolean existsByUserIdAndCompanyId(String authHeader, String companyId) {

        Long userId = jwtTokenProvider.tokenIdForController(authHeader);
        return userService.existsByUserIdAndCompanyId(userId, companyId);
    }
}

//Задача цього компонента перевіряти доступність користувача до інших компаній.
//        Виключити випадки коли користувач може під своїм токеном робити запити на інші компанії
//    Окрім загального фільтру тепер до кожного запиту в контройлер, додається токен та перевіряється
//        чи є зв'язок цього користувача в базі по ManyToMany зв'язку. JPA генерує запит перевіряє та надсилає
//        відповідь true/false а далі користувач або залишається не авторизованим або переходить до
//        ! Рекомендовано в майбутньому розглянути інший варіант перевірки так як ми створюємо таким чином додаткове
//        навантаження на Базу
