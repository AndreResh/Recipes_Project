package recipes.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.domain.Recipe;
import recipes.service.RecipeService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/recipe")
public class RecipeController {
    private final RecipeService service;

    public RecipeController(RecipeService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable("id") Integer id) {
        log.info("GET RECIPE with id {}", id);
        Optional<Recipe> recipe = service.findRecipeById(id);
        if (!recipe.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(recipe.get(), HttpStatus.OK);
        }

    }

    @PostMapping("/new")
    public ResponseEntity<?> saveRecipe(@Valid @RequestBody Recipe recipe,@AuthenticationPrincipal UserDetails details) {
        log.info("SAVE RECIPE: {}. With author: {}", recipe,details.getUsername());
        Integer id = service.addRecipe(recipe, details.getUsername());
        return new ResponseEntity<>(Map.of("id", id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable("id") Integer id,@AuthenticationPrincipal UserDetails details) {
        log.info("DELETE RECIPE with id {}. Author: {}", id,details.getUsername());
        Optional<Recipe> recipe = service.findRecipeById(id);
        if (!recipe.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            service.delete(id, details.getUsername());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody Recipe recipe,@AuthenticationPrincipal UserDetails details) {
        log.info("UPDATE RECIPE: {}, Principal name: {}", recipe,details.getUsername());
        Optional<Recipe> recipe1 = service.findRecipeById(id);
        if (!recipe1.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            if(!recipe1.get().getAuthor().equals(details.getUsername())){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            recipe.setId(id);
            service.addRecipe(recipe, details.getUsername());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> getRecipes(@RequestParam(name = "category", required = false) String category,
                                        @RequestParam(name = "name", required = false) String name) {
        log.info("GET RECIPE with category - {}, name - {}", category, name);
        List<Recipe> list;
        if ((!Objects.isNull(category) && !Objects.isNull(name)) || (Objects.isNull(category) && Objects.isNull(name))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else if (!Objects.isNull(category)) {
            list = service.getRecipesByCategory(category);
        } else if (!Objects.isNull(name)) {
            list = service.getRecipesByName(name);
        } else {
            list = Collections.emptyList();
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}