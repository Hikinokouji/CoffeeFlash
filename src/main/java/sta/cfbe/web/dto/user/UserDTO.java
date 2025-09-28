package sta.cfbe.web.dto.user;


//import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserDTO {

    private Long id;

    @NotNull
    private String phoneNumber;

    //Вказуємо що ми не буде передавати в ДТО пароль на вихід, тільки приймати, таким чином він в JSON не потрапить
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    //Вказує максимальну довжину
    @Length(max=255, message = "...")
    private String password;

}
