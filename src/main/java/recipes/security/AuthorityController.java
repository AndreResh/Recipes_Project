package recipes.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api")
public class AuthorityController {
    private final UserService service;
    private final PasswordEncoder encoder;

    public AuthorityController(UserService service, PasswordEncoder encoder) {
        this.service = service;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        log.info("POST USER: {}",user);
        if(service.findUserByEmail(user.getEmail())!=null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        user.setPassword(encoder.encode(user.getPassword()));
        service.saveUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
