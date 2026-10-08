package DDL.LAB.Backend.auth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {

        if (userRepository.findByUsername("admin").isEmpty()) {

            User user = new User();

            user.setUsername("admin");
            user.setPassword("admin123");
            user.setName("DDL LAB Admin");

            userRepository.save(user);
        }
    }
}