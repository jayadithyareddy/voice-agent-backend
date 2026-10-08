package DDL.LAB.Backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public Map<String, Object> health() {

        Map<String, Object> response = new HashMap<>();

        response.put("success", true);
        response.put("status", "ok");
        response.put("message", "DDL LAB Java backend is running");

        return response;
    }
}