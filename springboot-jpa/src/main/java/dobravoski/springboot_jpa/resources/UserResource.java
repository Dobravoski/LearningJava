package dobravoski.springboot_jpa.resources;

import dobravoski.springboot_jpa.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

    @GetMapping
    public ResponseEntity<User> findAll() {
        User testUser = new User(1L, "Dobravoski", "dobravoski@gmail.com", "4599999999", "12345");
        return ResponseEntity.ok().body(testUser);
    }
}
