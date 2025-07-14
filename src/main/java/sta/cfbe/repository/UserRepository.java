package sta.cfbe.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sta.cfbe.domain.company.Company;
import sta.cfbe.domain.user.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.companies WHERE u.id = :id")
    Optional<User> findById(@Param("id") Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.companies WHERE u.phoneNumber = :phoneNumber")
    Optional<User> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    User save(User user);

    boolean existsByIdAndCompanies_CompanyUuid(Long userId, String companyUuid);

}
