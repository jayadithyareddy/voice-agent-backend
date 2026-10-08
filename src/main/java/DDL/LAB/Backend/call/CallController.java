package DDL.LAB.Backend.call;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/calls")
@CrossOrigin(origins = "https://voice-agent-frontend-10ao.onrender.com/")
public class CallController {

    private final CallRepository callRepository;

    public CallController(CallRepository callRepository) {
        this.callRepository = callRepository;
    }

    @GetMapping
    public List<Call> getCalls() {
        return callRepository.findAllByOrderByStartedAtDesc();
    }

    @GetMapping("/{id}")
    public Call getCall(@PathVariable Long id) {
        return callRepository.findById(id).orElse(null);
    }

    @GetMapping("/customer/{customerId}")
    public List<Call> getCustomerCalls(@PathVariable Long customerId) {
        return callRepository.findByCustomerIdOrderByStartedAtDesc(customerId);
    }

    @PostMapping
    public Call createCall(@RequestBody Call call) {

        if (call.getStartedAt() == null) {
            call.setStartedAt(LocalDateTime.now());
        }

        if (call.getStatus() == null || call.getStatus().isBlank()) {
            call.setStatus("Completed");
        }

        return callRepository.save(call);
    }

    @PutMapping("/{id}")
    public Call updateCall(
            @PathVariable Long id,
            @RequestBody Call call
    ) {

        Call existing = callRepository.findById(id).orElse(null);

        if (existing == null) {
            return null;
        }

        existing.setCustomerId(call.getCustomerId());
        existing.setPhone(call.getPhone());
        existing.setDuration(call.getDuration());
        existing.setStatus(call.getStatus());
        existing.setOutcome(call.getOutcome());
        existing.setTranscript(call.getTranscript());
        existing.setSummary(call.getSummary());
        existing.setRecordingUrl(call.getRecordingUrl());
        existing.setStartedAt(call.getStartedAt());
        existing.setEndedAt(call.getEndedAt());

        return callRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deleteCall(@PathVariable Long id) {
        callRepository.deleteById(id);
    }
}