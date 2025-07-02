package sta.cfbe.repository.impl;

import org.springframework.stereotype.Repository;
import sta.cfbe.domain.company.Company;
import sta.cfbe.repository.CompanyRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class CompanyRepositoryImpl implements CompanyRepository {


    @Override
    public Optional<Company> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Company> findByCompanyUUID(UUID company_uuid) {
        return Optional.empty();
    }

    @Override
    public void save(Company company) {

    }

    @Override
    public void update(Company company) {

    }

    @Override
    public void delete(Long id) {

    }
}
