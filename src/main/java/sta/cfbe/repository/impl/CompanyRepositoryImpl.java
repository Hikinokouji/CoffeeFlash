package sta.cfbe.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sta.cfbe.domain.company.Company;
import sta.cfbe.repository.CompanyRepository;
import sta.cfbe.repository.DataSourceConfig;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CompanyRepositoryImpl implements CompanyRepository {

    private final DataSourceConfig dataSourceConfig;

    private final String CREATE_COMPANY = "CREATE DATABASE ";

    private final String SAVE_IN_COMPANIES = """
            INSERT INTO personal.companies (company_uuid, createdate, company_name) 
            VALUES (?, ?, ?);
            """;

    @Override
    public void createDataBase(String companyUuid) {
        //findDataBaseByUuid();
        String sql = CREATE_COMPANY + "\"" + companyUuid + "\"";
        try(Connection connection = dataSourceConfig.getConnection();
            Statement statement = connection.createStatement()){
            statement.executeUpdate(sql);
            System.out.println("Created dataBase: " + companyUuid);
        }catch (SQLException exception){
            exception.printStackTrace();
        }
    }

    @Override
    public Optional<Company> saveInTable(String companyUuid) {
        try{
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(SAVE_IN_COMPANIES);
            statement.setString(1, companyUuid);
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.setString(3, "Моя компанія");
            statement.execute();
            System.out.println("Saved dataBase: " + companyUuid);

            Company company = new Company();
            company.setCompany_uuid(companyUuid);
            company.setCreatedate(Timestamp.valueOf(LocalDateTime.now()));
            company.setCompanyName("Моя компанія");

            return Optional.of(company);
        }catch (SQLException exception){
            exception.printStackTrace();
            return Optional.empty();
        }
    }


//    @Override
//    public Optional<Company> findById(Long id) {
//        return Optional.empty();
//    }
//
//    @Override
//    public Optional<Company> findByCompanyUUID(UUID company_uuid) {
//        return Optional.empty();
//    }
//
//    @Override
//    public void save(Company company) {
//
//    }
//
//    @Override
//    public void update(Company company) {
//
//    }
//
//    @Override
//    public void delete(Long id) {
//
//    }
}
