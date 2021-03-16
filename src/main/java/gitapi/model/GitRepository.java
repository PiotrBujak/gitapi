package gitapi.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GitRepository {
    @JsonAlias({"full_name"})
    private String fullName;
    @JsonAlias({"description"})
    private String description;
    @JsonAlias({"html_url"})
    private String cloneUrl;
    @JsonAlias({"stargazers_count"})
    private Integer stars;
    @JsonAlias({"created_at"})
    private String createdAt;
}
