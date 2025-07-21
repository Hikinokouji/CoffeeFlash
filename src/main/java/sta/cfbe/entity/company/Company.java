package sta.cfbe.entity.company;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import sta.cfbe.entity.user.User;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="companies", schema = "personal")
@Data
public class Company{
    @Id
    @Column(name = "company_uuid")
    private String companyUuid;

    @JsonIgnore
    @Column(name="createdate")
    private Timestamp createdate;

    @Column(name = "company_name")
    private String companyName;

    @JsonIgnore
    @ManyToMany(mappedBy = "companies")
    private Set<User> users =new HashSet<>();
}
