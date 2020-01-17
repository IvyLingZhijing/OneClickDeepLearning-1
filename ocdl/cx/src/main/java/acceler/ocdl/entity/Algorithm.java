package acceler.ocdl.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "algorithm")
public class Algorithm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "current_cached_version")
    @JsonProperty("current_cached_version")
    private Integer currentCachedVersion;

    @Column(name = "current_released_version")
    @JsonProperty("current_released_version")
    private Integer currentReleasedVersion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"algorithmList", "suffixList", "userList"})
    private Project project;

    @OneToMany(mappedBy = "algorithm")
    @JsonProperty("model_list")
    @JsonIgnoreProperties(value = "algorithm")
    private List<Model> modelList;

}