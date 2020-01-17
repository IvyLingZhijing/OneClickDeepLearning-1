package acceler.ocdl.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    @ColumnTransformer(
            read = "CAST(AES_DECRYPT(UNHEX(password), '!@#$%^&') as char(128))",
            write = "HEX(AES_ENCRYPT(?, '!@#$%^&'))"
    )
    private String password;

    @Column(name = "source")
    private String source;

    @Column(name = "source_id")
    @JsonProperty("source_id")
    private String sourceId;

    @Column(name = "is_inner_user")
    @JsonProperty("is_inner_user")
    private Boolean isInnerUser;

    @ManyToMany
    @JoinTable(
            name = "r_user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    @JsonIgnoreProperties(value = "users")
    private List<Role> roles;

    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    private String updatedAt;

    @OneToMany(mappedBy = "user")
    @JsonProperty("user_data_list")
    @JsonIgnoreProperties(value = "user")
    private List<UserData> userDataList;

    @OneToMany(mappedBy = "owner")
    @JsonProperty("model_list")
    @JsonIgnoreProperties(value = {"owner", "lastOperator"})
    private List<Model> modelList;

    @OneToMany(mappedBy = "lastOperator")
    @JsonProperty("operate_model_list")
    @JsonIgnoreProperties(value = {"owner", "lastOperator"})
    private List<Model> operateModelList;

    @ManyToMany(mappedBy = "userList")
    @JsonProperty("project_list")
    @JsonIgnoreProperties(value = {"userList", "algorithmList", "suffixList"})
    private List<Project> projectList;

}