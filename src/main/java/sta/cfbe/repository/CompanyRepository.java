package sta.cfbe.repository;

import org.springframework.data.jpa.repository.Query;
import sta.cfbe.domain.company.Company;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {

    void createDataBase(String company_uuid);

    Optional<Company> saveInTable(String companyUuid);

    List<Company> findCompanyById(Long id);
}
