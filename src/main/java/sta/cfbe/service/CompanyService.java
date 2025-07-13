package sta.cfbe.service;

import sta.cfbe.domain.company.Company;

import java.util.Optional;

public interface CompanyService {

    Optional<Company> createCompany();

    Optional<Company> findCompanyById(String companyId);

}
