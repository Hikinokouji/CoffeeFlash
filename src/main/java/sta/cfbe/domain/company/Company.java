package sta.cfbe.domain.company;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import sta.cfbe.domain.user.User;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
@Entity
@Table(name="companies", schema = "personal")
@Data
public class Company{
    @Id
    private String company_uuid;
    @Column(name="createdate")
    private Timestamp createdate;
    @Column(name = "company_name")
    private String companyName;

    @ManyToMany(mappedBy = "companies")
    private Set<User> users =new HashSet<>();
}
