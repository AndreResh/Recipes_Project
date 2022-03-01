package recipes.security;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    public User save(User user);
    @Query("SELECT u FROM User u where u.email=:email")
    public User findAllByEmail(@Param("email") String email);
    public List<User> findAll();
}
