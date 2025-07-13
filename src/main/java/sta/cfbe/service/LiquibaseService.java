package sta.cfbe.service;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringResourceAccessor;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import lombok.RequiredArgsConstructor;

import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Service;

import java.sql.DriverManager;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class LiquibaseService {
    private final Environment environment;

    public void runLiquibaseForTenant(String tenantDbName) throws SQLException, LiquibaseException {
        String username = environment.getProperty("spring.datasource.username");
        String password = environment.getProperty("spring.datasource.password");
        String rawUrl = environment.getProperty("tenant.db-url-template");

        if (rawUrl == null) {
            throw new IllegalStateException("Missing property: tenant.db-url-template");
        }

        String tenantUrl = rawUrl.replace("{db}", tenantDbName);

        Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(
                        new JdbcConnection(DriverManager.getConnection(tenantUrl, username, password))
                );

        ResourceAccessor resourceAccessor = new SpringResourceAccessor(new DefaultResourceLoader());

        Liquibase liquibase = new Liquibase(
                "classpath:liquibase/master-tenant.yml",
                resourceAccessor,
                database
        );

        liquibase.update(new Contexts());
    }
}
