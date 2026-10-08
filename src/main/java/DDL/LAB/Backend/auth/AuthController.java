package DDL.LAB.Backend.auth;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "https://voice-agent-frontend-10ao.onrender.com/")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        User user = userRepository
                .findByUsername(request.username)
                .orElse(null);

        if (user == null) {
            return new LoginResponse(false, "Invalid username or password", null);
        }

        if (!user.getPassword().equals(request.password)) {
            return new LoginResponse(false, "Invalid username or password", null);
        }

        return new LoginResponse(true, "Login successful", user.getName());
    }

    public static class LoginRequest {

        public String username;
        public String password;
    }

    public static class LoginResponse {

        public boolean success;
        public String message;
        public String name;

        public LoginResponse(
                boolean success,
                String message,
                String name
        ) {
            this.success = success;
            this.message = message;
            this.name = name;
        }
    }
}