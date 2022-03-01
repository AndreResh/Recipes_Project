package recipes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import recipes.domain.Recipe;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Integer> {
    public Recipe save(Recipe recipe);

    public Optional<Recipe> findById(Integer id);

    public void deleteById(Integer id);

    public List<Recipe> findAll();

    @Query("SELECT r FROM Recipe r WHERE r.category=LOWER(:category) GROUP BY r.date")
    public List<Recipe> findAllByCategory(@Param("category") String category);

    @Query("SELECT r FROM Recipe r WHERE r.name=LOWER(:name) GROUP BY r.date")
    public List<Recipe> findAllByName(@Param("name") String name);
}
