package sta.cfbe.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sta.cfbe.domain.exeption.ResourceMappingException;
import sta.cfbe.domain.user.User;
import sta.cfbe.repository.DataSourceConfig;
import sta.cfbe.repository.UserRepository;
import sta.cfbe.repository.mappers.UserRowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final DataSourceConfig dataSourceConfig;

    private final String FIND_BY_ID = """
            SELECT u.id as id,
                   u.userphone as userphone,
                   u.password as password
            FROM personal.users u
            WHERE id = ?""";

    private final String FIND_BY_USERNAME = """
            SELECT u.id as id,
                   u.userphone as userphone,
                   u.password as password
            FROM personal.users u
            WHERE userphone = ?
            """;

    private final String UPDATE = """
            UPDATE personal.users
            SET userphone = ?,
                password = ?
            WHERE id = ?
            """;

    private final String CREATE = """
            INSERT INTO personal.users(userphone, password)
            VALUES (?, ?)
            """;

    private final String DELETE = """
            DELETE FROM personal.users
            WHERE id = ?
            """;


    @Override
    public Optional<User> findById(Long id) {
        try{
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            statement.setLong(1, id);
            try(ResultSet resultSet = statement.executeQuery()){
                return Optional.ofNullable(UserRowMapper.mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while finding user by id.");
        }
    }

    @Override
    public Optional<User> findByUsername(String userphone) {
        try{
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_USERNAME,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            statement.setString(1, userphone);
            try(ResultSet resultSet = statement.executeQuery()){
                return Optional.ofNullable(UserRowMapper.mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while finding user by userPhone.");
        }
    }

    @Override
    public void update(User user) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, user.getPhoneNumber());
            statement.setString(2, user.getPassword());
            statement.setLong(3, user.getId());
        } catch (SQLException e) {
            throw new ResourceMappingException("Exception while updating user.");
        }
    }

    @Override
    public void create(User user) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getPhoneNumber());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();
            try(ResultSet resultSet = statement.getGeneratedKeys()) {
                resultSet.next();
                user.setId(resultSet.getLong(1));
            }
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while updating user.");
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while delete user.");
        }
    }
}
