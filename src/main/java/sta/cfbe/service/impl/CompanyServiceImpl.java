package sta.cfbe.service.impl;

import liquibase.exception.LiquibaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sta.cfbe.domain.company.Company;
import sta.cfbe.repository.CompanyRepository;
import sta.cfbe.service.CompanyService;
import sta.cfbe.service.LiquibaseService;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final LiquibaseService liquibaseService;

    @Override
    public Optional<Company> createCompany() {
        String companyUuid = convetUUIDtoString(UUID.randomUUID());
        companyRepository.createDataBase(companyUuid);
        try {
            liquibaseService.runLiquibaseForTenant(companyUuid);
            return companyRepository.saveInTable(companyUuid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }

    private String convetUUIDtoString(UUID companyUuid){
        return companyUuid.toString().replace("-", "_");
    }
}
