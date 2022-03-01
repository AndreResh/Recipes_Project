package recipes.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import recipes.domain.Recipe;
import recipes.repository.RecipeRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RecipeService {
    private final RecipeRepository repository;


    public RecipeService(RecipeRepository repository) {
        this.repository = repository;
    }

    public Integer addRecipe(Recipe recipe,String name) {
        recipe.setAuthor(name);
        recipe.setDate(LocalDateTime.now());
        repository.save(recipe);
        return recipe.getId();
    }

    public Optional<Recipe> findRecipeById(Integer id) {
        return repository.findById(id);
    }

    public void delete(Integer id,String name) {
        if(repository.findById(id).get().getAuthor().equals(name)){
            repository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

    }

    public List<Recipe> getRecipesByCategory(String category) {
        List<Recipe> list = repository.findAll();
        List<Recipe> result = list.stream().filter(o -> o.getCategory().toLowerCase(Locale.ROOT).equals(category.toLowerCase()))
                .sorted((o1, o2) -> o2.getDate().compareTo(o1.getDate())).collect(Collectors.toList());

        return list.isEmpty() ? Collections.emptyList() : result;
    }

    public List<Recipe> getRecipesByName(String name) {
        List<Recipe> list = repository.findAll();
        List<Recipe> result = list.stream().filter(o -> o.getName().toLowerCase(Locale.ROOT).contains(name.toLowerCase()))
                .sorted((o1, o2) -> o2.getDate().compareTo(o1.getDate())).collect(Collectors.toList());
        return list.isEmpty() ? Collections.emptyList() : result;
    }
}
