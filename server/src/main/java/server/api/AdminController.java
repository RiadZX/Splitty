package server.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final String password;

    public AdminController() {
        password = UUID.randomUUID().toString().substring(0, 8);
        System.out.println("Password is: " + password);
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<String> generatePassword() {
        System.out.println("Password is: " + password);
        return ResponseEntity.ok("You can check the server console");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody String password) {
        System.out.println("Tried password:"+password);
        if (password.equals(this.password)) {
            return ResponseEntity.ok(password);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Your password is invalid.");
    }
}
