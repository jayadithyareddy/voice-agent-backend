package DDL.LAB.Backend.notification;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "https://voice-agent-frontend-10ao.onrender.com/")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @GetMapping
    public List<Notification> getNotifications() {
        return notificationRepository.findAllByOrderByCreatedAtDesc();
    }

    @PostMapping
    public Notification createNotification(
            @RequestBody Notification notification
    ) {

        notification.setId(null);
        notification.setCreatedAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    @PutMapping("/{id}/read")
    public Notification markAsRead(@PathVariable Long id) {

        Notification notification =
                notificationRepository.findById(id).orElse(null);

        if (notification == null) {
            return null;
        }

        notification.setRead(true);

        return notificationRepository.save(notification);
    }

    @DeleteMapping("/{id}")
    public void deleteNotification(@PathVariable Long id) {
        notificationRepository.deleteById(id);
    }
}