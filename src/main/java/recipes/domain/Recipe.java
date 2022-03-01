package recipes.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recipe")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonIgnore
    private Integer id;
    @NotBlank
    @Column(name = "name")
    private String name;
    @NotBlank
    @Column(name = "category")
    private String category;
    @Column(name = "date")
    private LocalDateTime date;
    @NotBlank
    @Column(name = "description")
    private String description;
    @ElementCollection
    @Size(min = 1)
    @NotNull
    private List<String> ingredients;
    @ElementCollection
    @Size(min = 1)
    @NotNull
    private List<String> directions;
    @Column(name = "author")
    @JsonIgnore
    private String author;

}
