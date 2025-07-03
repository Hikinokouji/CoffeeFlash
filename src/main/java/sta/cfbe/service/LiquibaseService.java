package sta.cfbe.service;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.sql.DriverManager;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class LiquibaseService {
    private final Environment environment;

    public void runLiquibaseForTenant(String tenantDbUrl) throws SQLException, LiquibaseException {
        String username = environment.getProperty("spring.datasource.username");
        String password = environment.getProperty("spring.datasource.password");
        String rawUrl = environment.getProperty("tenant.db-url-tempalte");
        String tenantUrl = rawUrl.replace("{db}", tenantDbUrl);

        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(
                new JdbcConnection(DriverManager.getConnection(tenantUrl, username, password))
        );

        Liquibase liquibase = new Liquibase(
                "liquibase/master-tenant.yml",
                new ClassLoaderResourceAccessor(),
                database
        );

        liquibase.update(new Contexts());
    }
}
