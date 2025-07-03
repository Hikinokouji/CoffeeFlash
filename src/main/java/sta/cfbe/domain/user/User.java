package sta.cfbe.domain.user;

import jakarta.persistence.*;
import lombok.Data;
import sta.cfbe.domain.company.Company;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", schema="personal")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="phonenumber" , unique = true, nullable = false)
    private String phoneNumber;
    @Column(name="password" , unique = true, nullable = false)
    private String password;

    @ManyToMany
    @JoinTable(
            name="user_company",
            schema="personal",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "company_uuid")
    )
    private Set<Company> companies = new HashSet<>();

}
