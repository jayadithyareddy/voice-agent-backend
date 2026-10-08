package DDL.LAB.Backend.followup;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/followups")
@CrossOrigin(origins = "https://voice-agent-frontend-10ao.onrender.com/")
public class FollowUpController {

    private final FollowUpRepository followUpRepository;

    public FollowUpController(FollowUpRepository followUpRepository) {
        this.followUpRepository = followUpRepository;
    }

    @GetMapping
    public List<FollowUp> getFollowUps() {
        return followUpRepository.findAllByOrderByDueDateAsc();
    }

    @GetMapping("/{id}")
    public FollowUp getFollowUp(@PathVariable Long id) {
        return followUpRepository.findById(id).orElse(null);
    }

    @GetMapping("/customer/{customerId}")
    public List<FollowUp> getCustomerFollowUps(
            @PathVariable Long customerId
    ) {
        return followUpRepository.findByCustomerIdOrderByDueDateAsc(customerId);
    }

    @PostMapping
    public FollowUp createFollowUp(@RequestBody FollowUp followUp) {

        if (followUp.getStatus() == null || followUp.getStatus().isBlank()) {
            followUp.setStatus("Pending");
        }

        return followUpRepository.save(followUp);
    }

    @PutMapping("/{id}")
    public FollowUp updateFollowUp(
            @PathVariable Long id,
            @RequestBody FollowUp followUp
    ) {

        FollowUp existing = followUpRepository.findById(id).orElse(null);

        if (existing == null) {
            return null;
        }

        existing.setCustomerId(followUp.getCustomerId());
        existing.setCallId(followUp.getCallId());
        existing.setTitle(followUp.getTitle());
        existing.setNotes(followUp.getNotes());
        existing.setStatus(followUp.getStatus());
        existing.setDueDate(followUp.getDueDate());
        existing.setCompletedAt(followUp.getCompletedAt());

        return followUpRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deleteFollowUp(@PathVariable Long id) {
        followUpRepository.deleteById(id);
    }
}