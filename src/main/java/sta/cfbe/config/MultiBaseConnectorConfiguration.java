package sta.cfbe.config;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class MultiBaseConnectorConfiguration {
    private final Environment environment;

    public Connection getCustomConnection(String db) throws SQLException {
        String dbUsername = environment.getProperty("spring.datasource.username");
        String dbPassword = environment.getProperty("spring.datasource.password");
        String dbUrl = environment.getProperty("spring.datasource.url") + db;
        return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
    }
}
