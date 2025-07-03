package sta.cfbe.repository;

import sta.cfbe.domain.company.Company;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {

    void createDataBase(String company_uuid);

    Optional<Company> saveInTable(String companyUuid);

//    Optional<Company> findById(Long id);
//
//    Optional<Company> findByCompanyUUID(UUID company_uuid);
//
//    void save(Company company);
//
//    void update(Company company);
//
//    void delete(Long id);
}
