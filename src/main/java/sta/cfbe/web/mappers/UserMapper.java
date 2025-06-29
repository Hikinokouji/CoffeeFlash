package sta.cfbe.web.mappers;

import org.mapstruct.Mapper;
import sta.cfbe.domain.user.User;
import sta.cfbe.web.dto.user.UserDTO;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toUserDTO(User user);

    User toEntity(UserDTO userDTO);

}
