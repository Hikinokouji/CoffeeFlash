package sta.cfbe.repository.mappers;

import lombok.SneakyThrows;
import sta.cfbe.domain.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper {

    @SneakyThrows
    public static User mapRow(ResultSet resultSet){
        if(resultSet.next()){
            User user = new User();
            user.setId(resultSet.getLong("id"));
            user.setPhoneNumber(resultSet.getString("userphone"));
            user.setPassword(resultSet.getString("password"));
            return user;
        }
        return null;
    }

}
