package sta.cfbe.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import sta.cfbe.domain.company.Company;
import sta.cfbe.repository.CompanyRepository;
import sta.cfbe.repository.DataSourceConfig;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CompanyRepositoryImpl implements CompanyRepository {

    private final DataSourceConfig dataSourceConfig;

    private final String FIND_ALL_COMPANY_BY_ID = """
            SELECT * FROM personal.companies c
                JOIN personal.user_company uc ON uc.company_uuid = c.company_uuid
                WHERE uc.user_id = ?
            """;

    private final String CREATE_COMPANY = "CREATE DATABASE ";

    private final String SAVE_IN_COMPANIES = """
            INSERT INTO personal.companies (company_uuid, createdate, company_name) 
            VALUES (?, ?, ?);
            """;

    private final String FIND_COMPANY_BY_UUID = """
            SELECT * FROM personal.companies c
            WHERE company_uuid = ?
            """;

    private final String FIND_COMPANY_BY_USER_ID = """
            SELECT company_uuid
            FROM personal.user_company
            WHERE user_id = ?
            """;



    @Override
    public void createDataBase(String companyUuid) {
        String sql = CREATE_COMPANY + "\"" + companyUuid + "\"";
        try(Connection connection = dataSourceConfig.getConnection();
            Statement statement = connection.createStatement()){
            statement.executeUpdate(sql);
            log.info("Created Database " + companyUuid);
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
            company.setCompanyUuid(companyUuid);
            company.setCreatedate(Timestamp.valueOf(LocalDateTime.now()));
            company.setCompanyName("Моя компанія");

            return Optional.of(company);
        }catch (SQLException exception){
            exception.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<Company> findCompanyById(Long id) {
        List<Company> companies = new ArrayList<>();

        try (Connection connection = dataSourceConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_COMPANY_BY_ID)) {

            statement.setLong(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Company company = new Company();
                    company.setCompanyUuid(rs.getString("company_uuid"));
                    company.setCompanyName(rs.getString("company_name"));
                    companies.add(company);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return companies;
    }

    @Override
    public Optional<Company> findCompanyByUuid(String companyUuid) {
        try(Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_COMPANY_BY_UUID)) {
            statement.setString(1, companyUuid);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Company company = new Company();
                    company.setCompanyUuid(rs.getString("company_uuid"));
                    company.setCompanyName(rs.getTimestamp("createdate").toLocalDateTime().toString());
                    company.setCompanyName(rs.getString("company_name"));
                    return Optional.of(company);
                }
                return Optional.empty();
            }
        }catch (SQLException exception){
            exception.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Set<String> findCompaniesByUserId(Long userId) {
        try(Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_COMPANY_BY_USER_ID)){
            statement.setLong(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                Set<String> companies = new HashSet<>();
                while (rs.next()) {
                    companies.add(rs.getString("company_uuid"));
                }
                return companies;
            }
        }catch (SQLException exception){
            throw new RuntimeException(exception);
        }
    }


}
