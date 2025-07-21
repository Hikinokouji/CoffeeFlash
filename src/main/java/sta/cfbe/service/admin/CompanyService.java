package sta.cfbe.service.admin;

import liquibase.exception.LiquibaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sta.cfbe.entity.company.Company;
import sta.cfbe.repository.admins.CompanyRepository;
import sta.cfbe.service.configService.LiquibaseService;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final LiquibaseService liquibaseService;


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

    public Optional<Company> findCompanyById(String companyId) {
        return Optional.empty();
    }

    private String convetUUIDtoString(UUID companyUuid){
        return companyUuid.toString().replace("-", "_");
    }
}
