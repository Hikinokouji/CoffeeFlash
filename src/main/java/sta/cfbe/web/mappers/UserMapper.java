package sta.cfbe.web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sta.cfbe.domain.user.User;
import sta.cfbe.web.dto.user.UserDTO;
import sta.cfbe.web.dto.user.UserResponse;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toUserDTO(User user);

    @Mapping(source = "companies", target = "companySet")
    UserResponse toUserResponse(User user);

    User toEntity(UserDTO userDTO);

}
