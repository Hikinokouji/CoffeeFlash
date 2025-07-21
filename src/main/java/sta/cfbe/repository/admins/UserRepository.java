package sta.cfbe.repository.admins;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sta.cfbe.entity.user.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.companies WHERE u.id = :id")
    Optional<User> findById(@Param("id") Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.companies WHERE u.phoneNumber = :phoneNumber")
    Optional<User> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    User save(User user);

    boolean existsByIdAndCompanies_CompanyUuid(Long userId, String companyUuid);

}
